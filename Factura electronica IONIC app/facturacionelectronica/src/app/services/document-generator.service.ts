import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {CacheService} from 'ionic-cache';
import {StorageService} from './storage.service';
import {TokenService} from './token.service';
import {ActivitiesService} from './activities.service';


@Injectable({
    providedIn: 'root'
})
export class DocumentGeneratorService {

    constructor(private http: HttpClient, private router: Router,
                private cache: CacheService,
                private activitiesService: ActivitiesService,
                private storage: StorageService, private tokenService: TokenService) {
    }

    public async get(company, pos, office,
                     documentType, client,
                     sellCondition, creditDays,
                     paymentMethods, details,
                     selectedCurrency, exchangeRate) {
        const invoice = {
            companyId: company.id,
            officeId: office.id,
            posId: pos.id,
            documentType: documentType.code.toString().padStart(2, '0'),
            document: {
                clave: undefined,
                numeroConsecutivo: undefined,
                codigoActividad: undefined,
                fechaEmision: undefined,
                emisor: undefined,
                receptor: undefined,
                condicionVenta: undefined,
                plazoCredito: undefined,
                medioPago: undefined,
                detalleServicio: {lineaDetalle: []},
                resumenFactura: undefined
            },
        };
        const activities = await this.activitiesService.getByCompany(company.id);
        invoice.document.codigoActividad = activities[0].activityCode;
        invoice.document.fechaEmision = new Date();
        invoice.document.emisor = {
            nombre: company.companyName,
            identificacion: {
                tipo: company.identificationType.toString().padStart(2, '0'),
                numero: company.identificationNumber
            },
            ubicacion: {
                provincia: company.provinceId.toString(),
                canton: company.countyId.toString().padStart(2, '0'),
                distrito: company.districtId.toString().padStart(2, '0'),
                otrasSenas: 'N/A'
            },
            correoElectronico: company.contactEmail
        };

        invoice.document.receptor = {
            nombre: client.clientName,
            identificacion: {
                tipo: client.identificationType.toString().padStart(2, '0'),
                numero: client.identificationNumber
            },
            ubicacion: {
                provincia: client.province.toString(),
                canton: client.county.toString().padStart(2, '0'),
                distrito: client.district.toString().padStart(2, '0'),
                otrasSenas: 'N/A'
            },
            correoElectronico: client.contactEmail
        };

        invoice.document.condicionVenta = sellCondition.id;

        const today = new Date();
        const dueDate = new Date();
        dueDate.setDate(today.getDate() + creditDays);
        invoice.document.plazoCredito = dueDate.toISOString().slice(0, 10);

        invoice.document.medioPago = paymentMethods;

        invoice.document.detalleServicio.lineaDetalle = this.getInvoiceDetails(details);

        invoice.document.resumenFactura = this.getInvoiceSummary(invoice, details, selectedCurrency, exchangeRate);
        console.log('Document Type', documentType);
        const consecutiveResponse = JSON.parse(JSON.stringify(await this.getKey(company.id, office.id, pos.id, documentType.code)));
        invoice.document.clave = consecutiveResponse.key;
        invoice.document.numeroConsecutivo = consecutiveResponse.consecutive;
        return invoice;
    }

    public async getKey(companyId, officeId, posId, documentType) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/consecutives/generate?companyId=${companyId}&officeId=${officeId}
        &posId=${posId}&documentType=${documentType}&documentSituation=1`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    private getInvoiceDetails(details) {
        const result = [];
        let lineNumber = 1;
        details.forEach(detail => {

            const productAmountWithoutTax = (detail.product.productPrice * detail.quantity);
            const taxAmount = ((detail.product.productPrice * detail.quantity) * (detail.product.taxRate / 100));
            const line = {
                numeroLinea: lineNumber,
                codigo: '',
                cantidad: detail.quantity,
                unidadMedida: detail.product.unitMeasure,
                detalle: detail.product.productName,
                precioUnitario: detail.product.productPrice,
                montoTotal: productAmountWithoutTax,
                descuento: [
                    {
                        montoDescuento: 0,
                        naturalezaDescuento: 'N/A'
                    }
                ],
                subTotal: productAmountWithoutTax,
                impuesto: [
                    {
                        codigo: detail.product.taxType.toString().padStart(2, '0'),
                        codigoTarifa: detail.product.taxRateCode.toString().padStart(2, '0'),
                        tarifa: detail.product.taxRate,
                        monto: taxAmount
                    }
                ],
                impuestoNeto: taxAmount,
                montoTotalLinea: productAmountWithoutTax + taxAmount
            };

            result.push(line);

            lineNumber++;
        });

        return result;
    }

    private getInvoiceSummary(invoice, details, currency, exchangeRate) {
        let totalProductsWithTax = 0;
        let totalProductWithoutTax = 0;
        let totalTaxes = 0;

        details.forEach(d => {
            if (d.product.taxRate > 0) {
                totalProductsWithTax += (d.product.productPrice * d.quantity);
            }
        });

        details.forEach(d => {
            if (d.product.taxRate === 0) {
                totalProductWithoutTax += (d.product.productPrice * d.quantity);
            }
        });

        invoice.document.detalleServicio.lineaDetalle.forEach(line => {
            line.impuesto.forEach(rate => {
                totalTaxes += rate.monto;
            });
        });
        const summary = {
            codigoTipoMoneda: {
                codigoMoneda: currency.code,
                tipoCambio: currency.code === 'CRC' ? 1 : exchangeRate
            },
            totalServGravados: totalProductsWithTax,
            totalServExentos: 0,
            totalServExonerado: 0,
            totalMercanciasGravadas: 0,
            totalMercanciasExentas: totalProductWithoutTax,
            totalMercExonerada: 0,
            totalGravado: totalProductsWithTax,
            totalExento: totalProductWithoutTax,
            totalExonerado: 0,
            totalVenta: totalProductsWithTax + totalProductWithoutTax,
            totalDescuentos: 0,
            totalVentaNeta: totalProductsWithTax + totalProductWithoutTax,
            totalImpuesto: totalTaxes,
            totalIvadevuelto: 0,
            totalOtrosCargos: 0,
            totalComprobante: (totalProductsWithTax + totalTaxes) + totalProductWithoutTax,
        };

        return summary;
    }
}
