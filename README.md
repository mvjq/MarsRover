# ProbeSystem
## Sistema de navegação de Sondas espaciais

### Descrição

A descrição do problem se encontra neste [link](https://gist.github.com/elo7-developer/f0b91a7a98e5e65288b875ac6d376875)

### Premissas

- Foi utilizado um banco em memória (H2) como camada de persistência.
- É possivel cadastrar multiplas sondas e multiplos planetas.
- Um probe só consegue navegar num planeta cadastrado/já existente.
- Existe um sistema de colisões entre as sondas. Uma sonda não pode pousar sob a outra
 ou fazer algum movimento inválido que cause uma colisão.
- Qualquer operação que cause um colisao ou posicao invalida no planeta é tratado como exceção 
 e um código de erro é retornado.
- Para mudar uma sonda de planeta é necessário deletar uma sonda de um planeta e cadastra-la 
 novamente.
- Na criaçao de um planeta, as coordenadas criadas sempre serao |coordenadas|, ou seja uma faixa de 
 posições de (-x, -y) a (x, y);

### Executando

Na raiz do projeto:
`./gradlew bootRun ` 

### Swagger

O swagger contendo informação sobre os endpoints e schema se encontra mapeado na URL: http://localhost:8080/swagger-ui/index.html
(o projeto deve estar em execução)

### Melhorias Pós Entrega

Criei uma branch chamada `refactoring` que contém as seguintes modificacoes:
- Utilizei o design pattern 'command' para lidar com comandos do Rover
- Implementei a logica de mudar de um rover de planeta (feito a partir de um POST no endpoint /v1/rover
com um rover pre existente no banco de dados e um planeta diferente do cadastrado).
- Refatoracao dos objetos deixando mais claro as responsabilidade entre eles.