new in

3.0.0
-----

  * cleanup SkysailApplication
  * in SkysailApplication: removed 	public abstract EventAdmin getEventAdmin();
  * removed WebComponentCall
  
2.3.0
-----

  * https://github.com/evandor/skysail/issues/3 - link to raml file
  
2.2.0
-----

  * testing release pipeline

2.1.0
-----

  * testing release pipeline

2.0.0
------

  * using new domain model with resources
  
1.0.0
------

 * new method SkysailApplication#createStaticDirectory() to add static content from inside the bundle
 * implemented: CORS config (https://github.com/evandor/skysail/issues/1)
 * implemented: removed polymer in bootstrap theme (https://github.com/evandor/skysail/issues/2)
 * removed AddApiVersionHeaderFilter
 * OptionalEncryptionFilter
 * removed AtomicReferences for OSGi services
 * removing shiro dependencies