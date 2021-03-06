/**
 * 
 */
package org.mcplissken.osgi.vertx;

import java.util.Map;

import org.mcplissken.gateway.restful.filter.RESTfulFilterFactory;
import org.mcplissken.gateway.vertx.VertxGateway;
import org.mcplissken.repository.ModelRepository;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 19, 2014
 */
public interface VertxService {
	
	public void deployModule(String moduleName, Map<String, Object> config);
	
	/**
	 * @param host
	 * @param port
	 * @param fileRoot
	 * @param webRoot
	 * @param webRootRegEx
	 * @param contentService
	 * @param contentRoot
	 * @param contentRootRegEx
	 * @param contentUsername
	 * @param contentPassword
	 * @param filtersFactoryMap
	 * @return
	 */
	VertxGateway vertxGateway(String host, int port, String fileRoot,
			String webRoot, String webRootRegEx, ModelRepository repository,
			String contentRoot, String contentRootRegEx, String oauthRootRegEx,
			Map<String, RESTfulFilterFactory> filtersFactoryMap);
}
