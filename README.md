<!-- ![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge) -->

# Instruções

![](/quarkus-microservices\Arquivos\projeto-1FASE.drawio.png)

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