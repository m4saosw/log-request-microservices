# Config Server

Log Request Application.

Config Server é o servidor de configurações.

## Tecnologias
    Java 8
    Spring Framework (Web, Eureka Client, Cloud Netflix Zuul, Cloud Config Server)
	Maven


## Execução
    $ spring-boot:run

## Web Interfaces

### Endpoints:
#### Obtem as configurações de um serviço registrado no servidor  (comando nativo)

	http://localhost:8888/log-request-service/default	
	http://localhost:8888/log-request-import-service/default	

### Configurações no ambiente
	Necessario inserir a pasta de configurações no ambiente:

	C:\log-request-microservices-config-repo
		log-request-service.properties
		log-request-import-service.properties