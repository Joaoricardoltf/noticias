<h1>Noticias</h1>

> Status: Done ✔️

<h3>Programa para pegar últimas notícias do site https://www.infomoney.com.br/mercados/</h3>

Para o desenvolvimento do projeto foi utilizado:
+ Jsoup, que é uma biblioteca Java de código aberto projetada para analisar, extrair e manipular dados armazenados em documentos HTML.
+ Gson, que é uma biblioteca do Google utilizada, entre outras coisas, na conversão de objetos Java em representação JSON.

<h2>Como funciona o programa</h2>

1) Dentro de um "for" será passada a variavel "pagina", para o método downloadHtmlMercados, onde será chamado 3 vezes, simulando o click no botão "carregar mais"

2) Recupera informações das tags <meta> e <time> de cada noticia

3) Converte a data que é recebia da noticia(que está no formato Timestamp) para o formato DIA/MES/ANO HORA:MINUTO

4) Recupera a informação do author atraves da tag <span> com class author-name, onde o primeiro filho é a tag "a"

5) Recupera todos os elementos "p" do primeiro nivel da tag "div" com o class col-md-9.col-lg-8.col-xl-6.m-sm-auto.m-lg-0.article-content

6) System.out.println para cada elemento que foi requerido (URL da notícia, título da notícia, subtítulo da notícia, autor, a data de publicação no formato (dia/mês/ano hora:minuto e o conteúdo da notícia, sem tags html e sem quebra de linha

