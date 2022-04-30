<!-- ![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge) -->

Washington Machado do Nascimento
Kevin Fernando de Souza Bispo
Patrick Rodrigues Costa
Hugo duran Iroz

# Instruções

![](./Arquivos/projeto-1FASE.drawio.png)

Tivemos problemas com o Docker compose(o Token do RHSSO não funcionava corretamente, por isso comentamos a subida dos serviços no arquivo). 
Vamos executar o docker compose da raiz para levantar os serviços de RHSSO, Rabbit é Jaegger. 
comando: 
```
docker-compose up
```
Depois entramos nas pastas de cartaocredito, contacorrente é investimentos é em cada uma delas executamos o comando 
```
quarkus dev
```
Depois de todos os serviços no ar, precisamos criar o token para ser utilizado na chamada dos serviços.
Para criar o token com acesso "admin", executamos o seguinte endpoint:
```
curl --location --request POST 'http://localhost:10080/auth/realms/Quarkus/protocol/openid-connect/token' \
--header 'content-type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic Y3VzdG9tZXItYXBwOjVmZmIzNDkwLTRkN2ItNDJlZC04Y2FjLWU2Nzc0NTUwYmM5Mg==' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=admin' \
--data-urlencode 'grant_type=password'
```
Para obter o token de acesso "user":
```
curl --location --request POST 'http://localhost:10080/auth/realms/Quarkus/protocol/openid-connect/token' \
--header 'content-type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic Y3VzdG9tZXItYXBwOjVmZmIzNDkwLTRkN2ItNDJlZC04Y2FjLWU2Nzc0NTUwYmM5Mg==' \
--data-urlencode 'username=user1' \
--data-urlencode 'password=user1' \
--data-urlencode 'grant_type=password'
```

Com o token, vamos executar o endpoint que da de alta uma conta corrente:
```
curl --location --request POST 'http://localhost:8080/contas' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "nome":"Conta numero 1" ,
    "idConta": 1,
    "agencia": 1234,
    "conta": 12345678,
    "saldo":1100.23
}'
```
Agora podemos executar a criação de Cartao de credito é investimentos, tem que estar associados a um id de conta valido.
```
curl --location --request POST 'http://localhost:8180/cartaoCredito' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "PAM":"123456789" ,
    "idConta": 1,
    "idCartaoCredito":1,
    "limite": 5000.00,
    "saldo":150.23
}
'
```
Investimentos:
```
curl --location --request POST 'http://localhost:8181/investimentos' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "nomeInvestimento":"Poupança" ,
    "idConta": 1,
    "idInvestimento": 1,
    "saldo":700.00
}
'
```
Em investimentos podemos aplicar o resgatar investimentos, isto vai afetar o saldo da conta de investimentos e da conta corrente:
Executamos o endpoint de Saldo de conta corrente previamente:
```
curl --location --request GET 'http://localhost:8080/contas/saldo/id/1' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN'
```
Agora executamos uma aplicação :
```
curl --location --request PUT 'http://localhost:8181/investimentos/id/1/aplicacao' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "valor":30.0
}'
```
Se executamos novamente o endpoint do Saldo das contas, veremos a alteração. 

Executando um resgate:

```
curl --location --request PUT 'http://localhost:8181/investimentos/id/1/resgate' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "valor":20.0
}'
```
No endpoint de cartao de credito podemos realizar o mesmo:

Credito:
```
curl --location --request PUT 'http://localhost:8180/cartaoCredito/id/1/credito' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "valor":45.0
}'
```
Debito:
Credito:
```
curl --location --request PUT 'http://localhost:8180/cartaoCredito/id/1/debito' \
--header 'Authorization: Bearer INSERIR AQUI O TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "valor":30.0
}'
```

Para monitoreamento podemos entrar na URL do Jaegger:
```
http://localhost:16686/search
```