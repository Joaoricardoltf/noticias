package code;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NoticiasJsoup {
	public static void main(String[] args) throws IOException, InterruptedException, ParseException {

		for (int pagina = 1; pagina < 4; pagina++) {

			//Passando a variavel "pagina", para o método downloadHtmlMercados, onde será chamado 3 vezes, simulando o click no botão "carregar mais"
			NoticiasDTO response = downloadHtmlMercados(pagina);
			for (Entry<String, JsonElement> s : response.postflair.entrySet()) {
				Document doc = downloadHtmlNoticia(s.getKey());
				
				//Recuperando informações das tags <meta> e <time> de cada noticia
				String URL = doc.select("meta[property=og:url]").get(0).attr("content");
				String titulo = doc.select("meta[property=og:title]").get(0).attr("content");
				String subtitulo = doc.select("meta[property=og:description]").get(0).attr("content");
				String horaPublicada = doc.select("time").get(0).attr("datetime");

				//Converte a data que é recebia da noticia(que está no formato Timestamp) para o formato DIA/MES/ANO HORA:MINUTO
				SimpleDateFormat formatoInicialData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				SimpleDateFormat formatoFinalData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				Date horaDataPublicacao = formatoInicialData.parse(horaPublicada);
				String horaPublicadaFinal = formatoFinalData.format(horaDataPublicacao);
				
				//Recuperando a informação do author atraves da tag <span> com class author-name, onde o primeiro filho é a tag <a>
				String autor = doc.select("span.author-name a").first().text();
				
				//Recuperando todos os elementos <p> do primeiro nivel da tag <div> com o class col-md-9.col-lg-8.col-xl-6.m-sm-auto.m-lg-0.article-content
				Elements conteudo = doc.select("div.col-md-9.col-lg-8.col-xl-6.m-sm-auto.m-lg-0.article-content > p");

				System.out.println("URL: " + URL);
				System.out.println("Titulo: " + titulo);
				System.out.println("Subtitulo: " + subtitulo);
				System.out.println("Hora: " + horaPublicadaFinal);
				System.out.println("Autor: " + autor);
				System.out.println("Conteudo: ");
				for (Element e : conteudo) {
					System.out.println(e.text());
				}
				System.out.println("--------------------------------------------------------------------");
				System.out.println("");
			}
		}

	}

	//DOWNLOAD DAS NOTICIAS PAGINADAS
	private static NoticiasDTO downloadHtmlMercados(int pagina) throws IOException, InterruptedException {
		HttpClient clientPagina = HttpClient.newHttpClient();
		HttpRequest requestPagina = HttpRequest.newBuilder()
				.uri(URI.create("https://www.infomoney.com.br/?infinity=scrolling"))
				.setHeader("authority", "application/json")
				.setHeader("sec-ch-ua", " Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"")
				.setHeader("x-requested-with", "XMLHttpRequest").setHeader("sec-ch-ua-mobile", "?0")
				.setHeader("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36")
				.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
				.setHeader("accept", "*/*").setHeader("origin", "https://www.infomoney.com.br")
				.setHeader("sec-fetch-site", "same-origin").setHeader("sec-fetch-mode", "cors")
				.setHeader("sec-fetch-dest", "empty").setHeader("referer", "https://www.infomoney.com.br/mercados/")
				.setHeader("accept-language", "en-US,en;q=0.9")
				.POST(HttpRequest.BodyPublishers.ofString(
						"action=infinite_scroll&page=" + pagina + "&order=DESC&query_args[category_name]=mercados"
								+ "&query_args[post_type][0]=post&query_args[post_type][1]=page"
								+ "&query_args[post_type][2]=colunistas&query_args[post_type][3]=patrocinados"
								+ "&query_args[post_type][4]=especiais&query_args[post_type][5]=web-story"))
				.build();
		String responsePagina = clientPagina.send(requestPagina, HttpResponse.BodyHandlers.ofString()).body();

		/**
		 * Converte a resposta API do Infomoney, que está no formato JSON, 
		 * utilizando um DTO(Data Transfer Object) atraves da biblioteca Gson do Google
		 */
		NoticiasDTO obj = new Gson().fromJson(responsePagina, NoticiasDTO.class);
		return obj;
	}

	/**
	 * Download de cada noticia
	 */
	private static Document downloadHtmlNoticia(String linkNoticia) throws IOException, InterruptedException {
		Document doc = Jsoup.connect(linkNoticia).get();
		return doc;
	}

	//Padrão de projeto - Data Transfer Object - Pesquisar no Google
	class NoticiasDTO {
		String type;
		String html;
		boolean lastbatch;
		String currentday;
		JsonObject postflair;
	}
}
