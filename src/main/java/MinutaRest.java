import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class MinutaRest {

	

	public static byte[] getParametros(String sigla_usuario, String[][] minuta) {
		byte[]  postDataBytes =null;
		try {
		Map<String,Object> params = new LinkedHashMap<>();
	        
		for (int i = 0; i < 1; i++) {
			params.put(minuta[i][0],minuta[i][1]);
		}
	//    params.put("sigla_usuario", sigla_usuario);
	    
	        

	        StringBuilder postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            
					postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        postDataBytes = postData.toString().getBytes("UTF-8");
	      
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		return postDataBytes;
	}
	public static String getMinutaSalvar (String sigla_usuario, String[][] minuta, String url_api_rest,String hashSenhaUsuarioSistema) {
		
		String jsonResponse = null;
	    if (url_api_rest!=null) {
	        try {
	           
	           URL object = new URL(url_api_rest);
	           HttpURLConnection connection = (HttpURLConnection) object.openConnection();
	           connection.setReadTimeout(60 * 1000);
	           connection.setConnectTimeout(60 * 1000);
	           connection.setRequestProperty("Authorization", hashSenhaUsuarioSistema);
	           connection.setDoOutput(true);
	           connection.getOutputStream().write(getParametros(sigla_usuario,minuta));
	           int responseCode = connection.getResponseCode();
	           String responseMsg = connection.getResponseMessage();

	            if (responseCode == 200) {
	                InputStream inputStr = connection.getInputStream();
	                String encoding = connection.getContentEncoding() == null ? "UTF-8"
	                        : connection.getContentEncoding();
	               jsonResponse = IOUtils.toString(inputStr, encoding);
	          
	            }
	        } catch (Exception e) {
	            e.printStackTrace();

	        }
	    }
	    return jsonResponse;
	    
	}
	
	public static String lerArquivo(String pathFile) throws IOException
	{
		 String texto =null;
		FileInputStream inputStream = new FileInputStream(pathFile);
	    try {
	         texto = IOUtils.toString(inputStream);
	    } finally {
	        inputStream.close();
	    }	
	    return texto;
	}
	
	public static String  montarUrl  (String usuario,String id_minuta) {
		String url_api_rest = "https://eproc-homologacao.jfrj.jus.br/eproc/controlador_rest.php/balcaovirtual/";
		url_api_rest+="usuario/"+usuario;
		url_api_rest+="/mesa/123";
		url_api_rest+="/documento/"+id_minuta;
		url_api_rest+="/salvar";
		return url_api_rest; 
	}
	public static void main (String[] args) throws IOException {
		String hashUsuarioSistema ="b58caac469c821d03f19b3c12aefaa97587d1d6a6a522731dface7b131e664c8";   
		
		
		String [][] minuta = new String [1][2];
		String arquivoMinuta ="C:\\Users\\bpd\\development\\JavaProjects\\BalcaoVirtualRest\\src\\main\\resources\\minuta.txt";
		minuta[0][0] = "html";  
		minuta[0][1] = lerArquivo(arquivoMinuta);
		
		String sigla_usuario="jrj16026";
		String id_minuta  = "511550149542490461195019914160";
		String url_api_rest = montarUrl(sigla_usuario,id_minuta);
		
		
	    System.out.print(getMinutaSalvar(sigla_usuario,minuta,url_api_rest,hashUsuarioSistema));
		
	}
	

	


	


}
