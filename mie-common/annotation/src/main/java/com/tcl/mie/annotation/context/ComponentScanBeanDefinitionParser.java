package com.tcl.mie.annotation.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcl.mie.annotation.IllegalConfigException;
import com.tcl.mie.annotation.handler.AbstractSpringAnnotationHandler;

/**
 * 
 * @author xinyizhang
 * @Date 2015-1-6 上午11:25:55
 */
public class ComponentScanBeanDefinitionParser extends org.springframework.context.annotation.ComponentScanBeanDefinitionParser {
	private static final String ANNOTATION_HANDLER_MAPPINGS_LOCATION = "META-INF/annotation.handlers";
	private Map<String, String> handlerMappings;

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String[] basePackages = StringUtils.commaDelimitedListToStringArray(element.getAttribute("base-package"));
		String[] libPaths = StringUtils.commaDelimitedListToStringArray(element.getAttribute("lib-path"));
		ClassAnnotationProcessor processor = parseProcessor(parserContext, element);

		ClassPathBeanDefinitionScanner scanner = (ClassPathBeanDefinitionScanner) configureScanner(parserContext, element);
		scanner.setLibPaths(libPaths);
		scanner.setClassAnnotationProcessor(processor);
		for (Class annotation : processor.getAnnotations()) {
			scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
		}
		Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
		registerComponents(parserContext.getReaderContext(), beanDefinitions, element);
		return null;
	}

	private ClassAnnotationProcessor parseProcessor(ParserContext parserContext, Element element) {
		List<AbstractSpringAnnotationHandler> handlers = new ArrayList<AbstractSpringAnnotationHandler>();
		
		Map<String, String> literalHandlers = getHandlerMappings();
		for (String literalHandler : literalHandlers.values()){
			try{
				AbstractSpringAnnotationHandler handler = (AbstractSpringAnnotationHandler) Class.forName(literalHandler.trim()).newInstance();
				handlers.add(handler);
			}catch (Exception e) {
				throw new IllegalConfigException(e);
			}
		}
		
		ClassAnnotationProcessor processor = new ClassAnnotationProcessor();
		for (AbstractSpringAnnotationHandler handler : handlers) {
			handler.setBeanDefinitionRegistry(parserContext.getRegistry());
			processor.registHandler(handler);
		}
		return processor;
	}

	private Map<String, String> getHandlerMappings() {
		if (this.handlerMappings == null) {
			try {
				Properties mappings = PropertiesLoaderUtils.loadAllProperties(ANNOTATION_HANDLER_MAPPINGS_LOCATION);
				this.handlerMappings = new HashMap(mappings);
			} catch (IOException ex) {
				IllegalStateException ise = new IllegalStateException("Unable to load Annotation Handler mappings from location ["
						+ ANNOTATION_HANDLER_MAPPINGS_LOCATION + "]");
				ise.initCause(ex);
				throw ise;
			}
		}
		return this.handlerMappings;
	}

	@Override
	protected org.springframework.context.annotation.ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
		return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters);
	}
}
