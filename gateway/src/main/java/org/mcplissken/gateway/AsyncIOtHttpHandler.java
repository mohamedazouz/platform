/**
 * 
 */
package org.mcplissken.gateway;

import org.mcplissken.gateway.restful.AsynchronusRESTfulRequestHandler;
import org.mcplissken.gateway.restful.RESTfulRequest;
import org.mcplissken.gateway.restful.RESTfulResponse;
import org.mcplissken.gateway.restful.ResponseObject;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.RESTfulException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 10, 2014
 */
public abstract  class AsyncIOtHttpHandler extends BasicHttpHandler{


	/* (non-Javadoc)
	 * @see com.mubasher.gateway.BasicHttpHandler#service(com.mubasher.gateway.HttpAdapter, com.mubasher.gateway.restful.RESTfulRequest, com.mubasher.gateway.restful.RESTfulResponse)
	 */
	@Override
	public void service(
			final HttpAdapter httpAdapter, 
			final RESTfulRequest request,
			final RESTfulResponse response) throws BadRequestException,
			UnauthorizedException {

		request.readDocumentObjectAsynchronously(new AsynchronusRESTfulRequestHandler() {

			@Override
			public void handleDocument(Object document) {

				try {
					
					ResponseObject responseObject = execute(httpAdapter, document);
					
					writeServiceResponse(httpAdapter, response, responseObject, false);
					
				} catch (RESTfulException e) {
					
					httpAdapter.exception(e);
				}

				
			}
		}, getDocumentType());

	}

	protected abstract ResponseObject execute(HttpAdapter adapter, Object document) throws RESTfulException;

	protected abstract Class<?> getDocumentType();

}
