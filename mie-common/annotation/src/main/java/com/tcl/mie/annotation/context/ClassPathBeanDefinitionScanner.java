package com.tcl.mie.annotation.context;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public class ClassPathBeanDefinitionScanner extends org.springframework.context.annotation.ClassPathBeanDefinitionScanner {
	private PathMatchingResourcePatternResolver4Jar resourcePatternResolver4Jar = new PathMatchingResourcePatternResolver4Jar();
	private MetadataReaderFactory metadataReaderFactory4Jar = new CachingMetadataReaderFactory(this.resourcePatternResolver4Jar);
	private BeanNameGenerator beanNameGenerator4Jar = new AnnotationBeanNameGenerator();
	private ScopeMetadataResolver scopeMetadataResolver4Jar = new AnnotationScopeMetadataResolver();
	private ClassAnnotationProcessor classAnnotationProcessor;
	private String[] libPaths;
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	public void setLibPaths(String[] libPath) {
		this.libPaths = libPath;
	}

	public void setClassAnnotationProcessor(ClassAnnotationProcessor classAnnotationProcessor) {
		this.classAnnotationProcessor = classAnnotationProcessor;
	}

	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}

	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> set = new LinkedHashSet<BeanDefinitionHolder>(32);
		set.addAll(super.doScan(basePackages));
		set.addAll(doScanInJar(basePackages));
		return set;
	}

	private Set<BeanDefinitionHolder> doScanInJar(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
		Set<BeanDefinition> candidates = findCandidateComponentsInJar(libPaths, basePackages);
		for (BeanDefinition candidate : candidates) {
			String beanName = this.beanNameGenerator4Jar.generateBeanName(candidate, getRegistry());
			if (candidate instanceof AbstractBeanDefinition) {
				postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
			}
			ScopeMetadata scopeMetadata = this.scopeMetadataResolver4Jar.resolveScopeMetadata(candidate);
			if (checkCandidate(beanName, candidate)) {
				BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
				definitionHolder = applyScope4Jar(definitionHolder, scopeMetadata);
				beanDefinitions.add(definitionHolder);
				registerBeanDefinition(definitionHolder, getRegistry());
			}
		}
		return beanDefinitions;
	}

	private Set<BeanDefinition> findCandidateComponentsInJar(String[] libpaths, String... basePackages) {
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>(32);
		String[] packageSearchPaths = new String[basePackages.length];
		for (int i = 0; i < basePackages.length; i++) {
			packageSearchPaths[i] = resolveBasePackage(basePackages[i]) + "/" + DEFAULT_RESOURCE_PATTERN;
		}
		try {
			Resource[] resources = this.resourcePatternResolver4Jar.getResources4Jar(libpaths, packageSearchPaths);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory4Jar.getMetadataReader(resource);
					if (isCandidateComponent(metadataReader)) {
						ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
						sbd.setResource(resource);
						sbd.setSource(resource);
						if (isCandidateComponent(sbd)) {
							candidates.add(sbd);
						}
					}
				}
			}
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}

	private BeanDefinitionHolder applyScope4Jar(BeanDefinitionHolder definitionHolder, ScopeMetadata scopeMetadata) {
		String scope = scopeMetadata.getScopeName();
		ScopedProxyMode scopedProxyMode = scopeMetadata.getScopedProxyMode();
		definitionHolder.getBeanDefinition().setScope(scope);
		if (BeanDefinition.SCOPE_SINGLETON.equals(scope) || BeanDefinition.SCOPE_PROTOTYPE.equals(scope) || scopedProxyMode.equals(ScopedProxyMode.NO)) {
			return definitionHolder;
		}
		boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
		return ScopedProxyUtils.createScopedProxy(definitionHolder, getRegistry(), proxyTargetClass);
	}

	@Override
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		return super.findCandidateComponents(basePackage);
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return true;
	}

	@Override
	protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		classAnnotationProcessor.process(definitionHolder, registry);
	}

}
