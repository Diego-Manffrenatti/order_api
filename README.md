Infelizmente, não tive tempo hábil para melhorar a cobertura dos testes e observabilidade (Configurações criadas).

O projeto utiliza Spring Boot, JPA, RabbitMQ, PostgreSQL e Docker, implementando comunicação assíncrona para garantir integração entre os serviços.

Arquitetura do Sistema
A API segue a estrutura hexagonal, dividida em:

Camada de Aplicação: Contém serviços e regras de negócio.
Camada de Adapters: Implementa interação com mensageria (RabbitMQ).
Camada de Domínio: Modelos e repositórios JPA.
Camada de Infraestrutura: Configurações de mensageria, persistência e segurança.

Tecnologias Utilizadas
Java 17
Spring Boot 3.4.3
Spring Data JPA
RabbitMQ
PostgreSQL
Docker e Docker Compose
JUnit 5 & Testcontainers

Execução do sistema

-Iniciar PostgreSQL via Docker:

docker-compose up -d

-Iniciar o RabbitMQ via Docker:

docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management

Acesso via

URL: http://localhost:15672
Usuário/Senha: guest/guest

-Rodar a Aplicação

docker build -t order-api .
docker run -p 8080:8080 order-api

- Interação com Mensageria (RabbitMQ)

Publicação de Eventos
A API publica eventos quando um pedido é criado ou alterado:

order.created.queue → Publicado ao criar um pedido.
order.status.updated.queue → Publicado ao alterar status.

Consumo de Eventos
O sistema escuta os seguintes eventos:

payment.approved.queue → Aprova pedido após pagamento.
order.cancelled.queue → Cancela pedido.

-Testando a API

Criar Order(POST /api/v1/orders)

{
"customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8",
"productId": "789e4567-e89b-12d3-a456-426614174114",
"category": "AUTO",
"salesChannel": "MOBILE",
"paymentMethod": "CREDIT_CARD",
"totalMonthlyPremiumAmount": 275000.50,
"insuredAmount": 75.25,
"coverages": {
"Roubo": 100000.25,
"Perda Total": 100000.25,
"Colisão com Terceiros": 75000.00
},
"assistances": [
"Guincho até 250km",
"Troca de Óleo"
]
}

Resposta Esperada:

{
"orderId": "2d3ec00d-3fdb-4c85-b87b-575171c015cc",
"customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8",
"productId": "789e4567-e89b-12d3-a456-426614174114",
"category": "AUTO",
"salesChannel": "MOBILE",
"paymentMethod": "CREDIT_CARD",
"totalMonthlyPremiumAmount": 275000.50,
"insuredAmount": 75.25,
"coverages": {
"Roubo": 100000.25,
"Perda Total": 100000.25,
"Colisão com Terceiros": 75000.00
},
"assistances": [
"Guincho até 250km",
"Troca de Óleo"
],
"status": "PENDING",
"createdAt": "2025-03-10T16:48:58.146989472",
"finishedAt": null,
"history": [
{
"status": "RECEIVED",
"timestamp": "2025-03-10T16:48:58.151905882"
},
{
"status": "VALIDATED",
"timestamp": "2025-03-10T16:48:59.281084413"
},
{
"status": "PENDING",
"timestamp": "2025-03-10T16:48:59.390292939"
}
]
}

Buscar Order por ID(GET /api/v1/orders/{id})
{
"orderId": "2d3ec00d-3fdb-4c85-b87b-575171c015cc",
"customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8",
"productId": "789e4567-e89b-12d3-a456-426614174114",
"category": "AUTO",
"salesChannel": "MOBILE",
"paymentMethod": "CREDIT_CARD",
"totalMonthlyPremiumAmount": 275000.50,
"insuredAmount": 75.25,
"coverages": {
"Roubo": 100000.25,
"Perda Total": 100000.25,
"Colisão com Terceiros": 75000.00
},
"assistances": [
"Guincho até 250km",
"Troca de Óleo"
],
"status": "PENDING",
"createdAt": "2025-03-10T16:48:58.146989",
"finishedAt": null,
"history": [
{
"status": "RECEIVED",
"timestamp": "2025-03-10T16:48:58.151905882"
},
{
"status": "VALIDATED",
"timestamp": "2025-03-10T16:48:59.281084413"
},
{
"status": "PENDING",
"timestamp": "2025-03-10T16:48:59.390292939"
}
]
}
Resposta Esperada:

Buscar Order por Cliente (GET /api/v1/orders/customer/{customerId})

Resposta Esperada:

[
{
"orderId": "e3f71a17-1cdb-463d-b3d4-997dd952a81c",
"customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8",
"productId": "789e4567-e89b-12d3-a456-426614174114",
"category": "AUTO",
"salesChannel": "MOBILE",
"paymentMethod": "CREDIT_CARD",
"totalMonthlyPremiumAmount": 275000.50,
"insuredAmount": 75.25,
"coverages": {
"Roubo": 100000.25,
"Perda Total": 100000.25,
"Colisão com Terceiros": 75000.00
},
"assistances": [
"Guincho até 250km",
"Troca de Óleo"
],
"status": "CANCELLED",
"createdAt": "2025-03-10T04:16:04.987592",
"finishedAt": null,
"history": [
{
"status": "RECEIVED",
"timestamp": "2025-03-10T16:48:58.151905882"
},
{
"status": "VALIDATED",
"timestamp": "2025-03-10T16:48:59.281084413"
},
{
"status": "PENDING",
"timestamp": "2025-03-10T16:48:59.390292939"
},
{
"status": "CANCELLED",
"timestamp": "2025-03-10T16:48:59.470191225"
}
]
},
{
"orderId": "2d3ec00d-3fdb-4c85-b87b-575171c015cc",
"customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8",
"productId": "789e4567-e89b-12d3-a456-426614174114",
"category": "AUTO",
"salesChannel": "MOBILE",
"paymentMethod": "CREDIT_CARD",
"totalMonthlyPremiumAmount": 275000.50,
"insuredAmount": 75.25,
"coverages": {
"Roubo": 100000.25,
"Perda Total": 100000.25,
"Colisão com Terceiros": 75000.00
},
"assistances": [
"Guincho até 250km",
"Troca de Óleo"
],
"status": "PENDING",
"createdAt": "2025-03-10T16:48:58.146989",
"finishedAt": null,
"history": [
{
"status": "RECEIVED",
"timestamp": "2025-03-10T16:48:58.151905882"
},
{
"status": "VALIDATED",
"timestamp": "2025-03-10T16:48:59.281084413"
},
{
"status": "PENDING",
"timestamp": "2025-03-10T16:48:59.390292939"
}]
}
]


Os eventos externos foram simulados publicando via inteface do RabbitMQ

Simular Evento Externo de Pagamento Aprovado
{
"orderId": "b1234e56-7890-4cde-8123-a4567f89abcd"
}'

Simular Evento Externo de Cancelamento
{
"customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8"
}'
