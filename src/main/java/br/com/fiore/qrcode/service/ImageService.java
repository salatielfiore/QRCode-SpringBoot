package br.com.fiore.qrcode.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import reactor.core.publisher.Mono;

@Service
@Cacheable(cacheNames = "qr-code-cache", sync = true)
public class ImageService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

	public Mono<byte[]> generateQRCode(String text, int width, int height){
		
		Assert.hasText(text, "o texto não deve esta vazio");
		Assert.isTrue(width > 0, "a largura deve ser maior que 0");
		Assert.isTrue(height > 0, "a altura deve ser maior que 0");
		
		return Mono.create(sink -> {
			//LOGGER.info("Will generate image  text=[{}], width=[{}], height=[{}]", text, width, height);
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
				MatrixToImageWriter.writeToStream(matrix, MediaType.IMAGE_PNG.getSubtype(), baos, new MatrixToImageConfig());
				sink.success(baos.toByteArray());
			} catch (IOException | WriterException ex) {
				sink.error(ex);
			}
		});
	}
	
//	@CacheEvict(cacheNames = "qr-code-cache", allEntries = true)
//	public void purgeCache() {
//		LOGGER.info("Purging cache");
//	}
}
