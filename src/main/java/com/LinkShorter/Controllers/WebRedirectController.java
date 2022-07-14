package com.LinkShorter.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class WebRedirectController {
    
    private final AzureCosmosDBController cosmosDB = new AzureCosmosDBController();

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void redirect(@PathVariable String id, HttpServletResponse resp) throws Exception {
		final String url = cosmosDB.getLink(id);
		if (url != null)
			resp.sendRedirect(url);
		else
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

}
