# API Gateway

Log Request Application.

API Gateway Zuul é um microserviço do tipo proxy. Este endereço do proxy é o único que deve ficar exposto para o cliente como ponto de acesso à aplicação.

## Tecnologias
    Java 8
    Spring Framework (Web, Eureka Client, Cloud Netflix Zuul)
	Maven

## Execução
    $ spring-boot:run

## Web Interfaces

### Endpoints:
#### Obtem as rotas para os serviços via proxy  (comando nativo)
	
	GET http://localhost:5555/actuator/routes
	
	Exemplo resultado:
	
	{
		"/log-request-service/**": "log-request-service",
		"/log-request-import-service/**": "log-request-import-service"
	}

#### Exemplo de chamada para o serviço de lista de log request  (via proxy)
	GET http://localhost:5555/log-request-service