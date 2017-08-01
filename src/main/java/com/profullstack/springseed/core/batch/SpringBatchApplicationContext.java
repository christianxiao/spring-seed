package com.profullstack.springseed.core.batch;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Assert;

public class SpringBatchApplicationContext extends GenericApplicationContext {

    private final AnnotatedBeanDefinitionReader annotatedBeanReader;

    private final XmlBeanDefinitionReader xmlBeanReader;

    public SpringBatchApplicationContext() {
        this(true);
    }

    public SpringBatchApplicationContext(boolean allowBeanDefinitionOverriding) {
        super();
        this.annotatedBeanReader = new AnnotatedBeanDefinitionReader(this);
        this.xmlBeanReader = new XmlBeanDefinitionReader(this);
        setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
    }

    public void registerAnnotationClasses(Class<?>... annotatedClasses) {
        Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
        this.annotatedBeanReader.register(annotatedClasses);
    }

    public void registerXmlClasses(String... xmlConfigLocations) {
        this.xmlBeanReader.loadBeanDefinitions(xmlConfigLocations);
    }

    public static SpringBatchApplicationContext instance(String jobPath) throws ClassNotFoundException {
        SpringBatchApplicationContext context = new SpringBatchApplicationContext();
        ApplicationContextInitializer initializer = new BatchApplicationContextInitializer();
        initializer.initialize(context);

        try{
            context.registerAnnotationClasses(Class.forName(jobPath));
        }catch (ClassNotFoundException cnfe){
            context.registerXmlClasses(jobPath);
        }

        context.refresh();
        return context;
    }
}
