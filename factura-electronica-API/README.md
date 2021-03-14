# Factura Electrónica Costa Rica
[![CodeFactor](https://www.codefactor.io/repository/github/ojimenezc/softcorp-fe/badge?s=72583cd36aade382ac850447c5cb95a4a31d1132)](https://www.codefactor.io/repository/github/ojimenezc/softcorp-fe)

El API de factura electrónica contiene todas las funciones necesarias para la facturación electrónica en Costa Rica.

# Características
Para conocer las opciones que provee el API puede revisar la documentación oficial en postman
https://documenter.getpostman.com/view/9434271/SzRw1r1p?version=latest

### Información técnica

Especificaciones técnicas:

* Spring boot 2.1.9.RELEASE
* Java 1.8
* Esquema de base de datos controlado con liquibase (Podrá configurar su propio motor de base de datos)
* Spring security (Podrá configurar su propio IDP)

### Instalación
El API ha de compilarse con los siguientes comandos

#### Para producción 

```sh
$ mvn clean
$ mvn verify
$ mvn package -P release
```

#### Para desarrollo 
En caso de necesitar saltarse la corrida de liquibase
```sh
$ mvn clean verify -Dmaven.test.skip=true -Dliquibase.should.run=false
$ mvn package -P dev
```
En caso de querer correr liquibase
```sh
$ mvn clean verify -Dmaven.test.skip=true -Dliquibase.should.run=true
$ mvn package -P dev
```
Con esto el .war resultante podrá ser ejecutado en el servidor web de su preferencia

#### Docker
El API contiene un archivo de docker para facilitar el despliegue, para usarlo ejecute el siguiente comando
```sh
    sudo docker build -t api/facturaelectronica:latest .
```

### Configuración
Toda la configuración necesaria se encuentra en el de resources en básicamente 2 archivos.
* El archivo application-dev.properties donde se configura el ambiente de desarrollo
* El archivo application-release.properties donde se configura el ambiente de producción

