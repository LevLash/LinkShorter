package com.LinkShorter.Controllers;

import com.LinkShorter.Entities.GenerateRequest;
import com.LinkShorter.Entities.Link;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class LinkShorterController {
	
	private final AzureCosmosDBController cosmosDB = new AzureCosmosDBController();
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> save(@RequestBody @Valid GenerateRequest req) {
		final String url = req.getLink();
		final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
		if (urlValidator.isValid(url)) {
			final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
			cosmosDB.addLink(new Link(id, url));
			return new ResponseEntity<>("http://localhost:8080/" + id, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
 
}
