/*
 * Copyright (c) 2011-2013 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package org.mcplissken.gateway.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class allows you to do route requests based on the HTTP verb and the request URI, in a manner similar
 * to <a href="http://www.sinatrarb.com/">Sinatra</a> or <a href="http://expressjs.com/">Express</a>.<p>
 * MubasherRouteMatcher also lets you extract parameters from the request URI either a simple pattern or using
 * regular expressions for more complex matches. Any parameters extracted will be added to the requests parameters
 * which will be available to you in your request handler.<p>
 * It's particularly useful when writing REST-ful web applications.<p>
 * To use a simple pattern to extract parameters simply prefix the parameter name in the pattern with a ':' (colon).<p>
 * Different handlers can be specified for each of the HTTP verbs, GET, POST, PUT, DELETE etc.<p>
 * For more complex matches regular expressions can be used in the pattern. When regular expressions are used, the extracted
 * parameters do not have a name, so they are put into the HTTP request with names of param0, param1, param2 etc.<p>
 * Multiple matches can be specified for each HTTP verb. In the case there are more than one matching patterns for
 * a particular request, the first matching one will be used.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RemovableRouteMatcher implements Handler<HttpServerRequest> {

	private final List<PatternBinding> getBindings = new ArrayList<>();
	private final List<PatternBinding> putBindings = new ArrayList<>();
	private final List<PatternBinding> postBindings = new ArrayList<>();
	private final List<PatternBinding> deleteBindings = new ArrayList<>();
	private final List<PatternBinding> optionsBindings = new ArrayList<>();
	private final List<PatternBinding> headBindings = new ArrayList<>();
	private final List<PatternBinding> traceBindings = new ArrayList<>();
	private final List<PatternBinding> connectBindings = new ArrayList<>();
	private final List<PatternBinding> patchBindings = new ArrayList<>();
	private Handler<HttpServerRequest> noMatchHandler;

	@Override
	public void handle(HttpServerRequest request) {
		switch (request.method()) {
		case "GET":
			route(request, getBindings);
			break;
		case "PUT":
			route(request, putBindings);
			break;
		case "POST":
			route(request, postBindings);
			break;
		case "DELETE":
			route(request, deleteBindings);
			break;
		case "OPTIONS":
			route(request, optionsBindings);
			break;
		case "HEAD":
			route(request, headBindings);
			break;
		case "TRACE":
			route(request, traceBindings);
			break;
		case "PATCH":
			route(request, patchBindings);
			break;
		case "CONNECT":
			route(request, connectBindings);
			break;
		}
	}

	public void removeHandler(String method, String path){
		
		switch (method) {
		case "GET":
			removeRoute(path, getBindings);
			break;
		case "PUT":
			removeRoute(path, putBindings);
			break;
		case "POST":
			removeRoute(path, postBindings);
			break;
		case "DELETE":
			removeRoute(path, deleteBindings);
			break;
		case "OPTIONS":
			removeRoute(path, optionsBindings);
			break;
		case "HEAD":
			removeRoute(path, headBindings);
			break;
		case "TRACE":
			removeRoute(path, traceBindings);
			break;
		case "PATCH":
			removeRoute(path, patchBindings);
			break;
		case "CONNECT":
			removeRoute(path, connectBindings);
			break;
		}
	}

	/**
	 * Specify a handler that will be called for a matching HTTP GET
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher get(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, getBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP PUT
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher put(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, putBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP POST
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher post(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, postBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP DELETE
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher delete(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, deleteBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP OPTIONS
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher options(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, optionsBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP HEAD
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher head(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, headBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP TRACE
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher trace(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, traceBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP CONNECT
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher connect(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, connectBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP PATCH
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher patch(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, patchBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for all HTTP methods
	 * @param pattern The simple pattern
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher all(String pattern, Handler<HttpServerRequest> handler) {
		addPattern(pattern, handler, getBindings);
		addPattern(pattern, handler, putBindings);
		addPattern(pattern, handler, postBindings);
		addPattern(pattern, handler, deleteBindings);
		addPattern(pattern, handler, optionsBindings);
		addPattern(pattern, handler, headBindings);
		addPattern(pattern, handler, traceBindings);
		addPattern(pattern, handler, connectBindings);
		addPattern(pattern, handler, patchBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP GET
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher getWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, getBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP PUT
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher putWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, putBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP POST
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher postWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, postBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP DELETE
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher deleteWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, deleteBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP OPTIONS
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher optionsWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, optionsBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP HEAD
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher headWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, headBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP TRACE
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher traceWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, traceBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP CONNECT
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher connectWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, connectBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for a matching HTTP PATCH
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher patchWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, patchBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called for all HTTP methods
	 * @param regex A regular expression
	 * @param handler The handler to call
	 */
	public RemovableRouteMatcher allWithRegEx(String regex, Handler<HttpServerRequest> handler) {
		addRegEx(regex, handler, getBindings);
		addRegEx(regex, handler, putBindings);
		addRegEx(regex, handler, postBindings);
		addRegEx(regex, handler, deleteBindings);
		addRegEx(regex, handler, optionsBindings);
		addRegEx(regex, handler, headBindings);
		addRegEx(regex, handler, traceBindings);
		addRegEx(regex, handler, connectBindings);
		addRegEx(regex, handler, patchBindings);
		return this;
	}

	/**
	 * Specify a handler that will be called when no other handlers match.
	 * If this handler is not specified default behaviour is to return a 404
	 */
	public RemovableRouteMatcher noMatch(Handler<HttpServerRequest> handler) {
		noMatchHandler = handler;
		return this;
	}


	private static void addPattern(String input, Handler<HttpServerRequest> handler, List<PatternBinding> bindings) {
		// We need to search for any :<token name> tokens in the String and replace them with named capture groups
		Matcher m =  Pattern.compile(":([A-Za-z][A-Za-z0-9_]*)").matcher(input);
		StringBuffer sb = new StringBuffer();
		Set<String> groups = new HashSet<>();
		while (m.find()) {
			String group = m.group().substring(1);
			if (groups.contains(group)) {
				throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
			}
			m.appendReplacement(sb, "(?<$1>[^\\/]+)");
			groups.add(group);
		}
		m.appendTail(sb);
		String regex = sb.toString();
		PatternBinding binding = new PatternBinding(Pattern.compile(regex), groups, handler);
		bindings.add(binding);
	}

	private static void addRegEx(String input, Handler<HttpServerRequest> handler, List<PatternBinding> bindings) {
		PatternBinding binding = new PatternBinding(Pattern.compile(input), null, handler);
		bindings.add(binding);
	}

	private void route(HttpServerRequest request, List<PatternBinding> bindings) {
		for (PatternBinding binding: bindings) {
			Matcher m = binding.pattern.matcher(request.path());
			if (m.matches()) {
				Map<String, String> params = new HashMap<>(m.groupCount());
				if (binding.paramNames != null) {
					// Named params
					for (String param: binding.paramNames) {
						params.put(param, m.group(param));
					}
				} else {
					// Un-named params
					for (int i = 0; i < m.groupCount(); i++) {
						params.put("param" + i, m.group(i + 1));
					}
				}
				request.params().add(params);
				binding.handler.handle(request);
				return;
			}
		}
		if (noMatchHandler != null) {
			noMatchHandler.handle(request);
		} else {
			// Default 404
			request.response().setStatusCode(404);
			request.response().end();
		}
	}

	private void removeRoute(String path, List<PatternBinding> bindings){
		
		PatternBinding bindingToRemove = null;
		
		for (PatternBinding binding: bindings) {

			Matcher m = binding.pattern.matcher(path);

			if (m.matches()) {
				bindingToRemove = binding;
				break;
			}
		}
		
		bindings.remove(bindingToRemove);

	}

	private static class PatternBinding {
		final Pattern pattern;
		final Handler<HttpServerRequest> handler;
		final Set<String> paramNames;

		private PatternBinding(Pattern pattern, Set<String> paramNames, Handler<HttpServerRequest> handler) {
			this.pattern = pattern;
			this.paramNames = paramNames;
			this.handler = handler;
		}
	}

}
