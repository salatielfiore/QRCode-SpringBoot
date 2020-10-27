package br.com.fiore.qrcode.controller;

import java.util.concurrent.TimeUnit;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fiore.qrcode.model.Cliente;
import br.com.fiore.qrcode.service.ImageService;
import reactor.core.publisher.Mono;

@Controller
@EnableCaching
@EnableScheduling
@SpringBootApplication
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.DELETE})
public class QrCodeController {
	
	public static final String QRCODE_ENDPOINT = "/qrcode";
	public static final long THIRTY_MINUTES = 1800000; 
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping(path = "/")
	public String index() {
		return "index";
	}
	
	
	@PostMapping(path = "/qr")
	public String getQrCode(@RequestParam("text") String text, Model model) {
		
		Cliente c = new Cliente();
		c.setNome("Salatiel fiore");
		c.setEmail("salatielfiore70@gmail.com");
		c.setTelefone("(11) 943908358");
		c.setEndereco("rua paulino nascimento n 410");
		
		model.addAttribute("text", c);
		
		return "qrcode";
	}
	
	
	
	@GetMapping(value = QRCODE_ENDPOINT, produces = MediaType.IMAGE_PNG_VALUE)
	public Mono<ResponseEntity<byte[]>> getQRCode(@PathParam("text") String text) {
		return imageService.generateQRCode(text, 256, 256).map(imageBuff ->  
			ResponseEntity.ok().cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES)).body(imageBuff)
		);
	}
	
//	@Scheduled(fixedRate = THIRTY_MINUTES)
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	@DeleteMapping(value = QRCODE_ENDPOINT)
//	public void deleteAllCachedImages() {
//		imageService.purgeCache();
//	}

}
