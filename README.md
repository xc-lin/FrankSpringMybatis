1. 首先我们可以看到springMybatis这个包是不参与Spring的包扫描的，然后主要的整合代码都在这个包中
2. AppConfig.java这个类中，我们注册了SqlSessionFactory这个Bean并定义了Spring的包扫描位置，以及一个自定义注解，其中包括`@Import(FrankImportBeanDefinitionRegister.class)`
   ```java
   @ComponentScan("com.lxc")
   @FrankMapperScan("com.lxc.mapper")
   public class AppConfig {
       @Bean
       public SqlSessionFactory sqlSessionFactory() throws IOException {
           InputStream resourceAsStream = Resources.getResourceAsStream("mybatis.xml");
           SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(resourceAsStream);
           return factory;
       }
   }
   ```
3. FrankMapperScan.java 这是一个自定义注解，其中包括`@Import(FrankImportBeanDefinitionRegister.class)`
   ```java
   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.TYPE)
   @Import(FrankImportBeanDefinitionRegister.class)
   public @interface FrankMapperScan {
          String value() default "";
   }
   ```
4. FrankFactoryBean.java 定义了一个factoryBean，用来生成各种Mapper的代理对象，如果仅仅只是把这个factoryBean放进了ioc容器中，那么也只会固定的生成两个对象一个factoryBean本身，一个是getObject()返回的对象。但是这样不够灵活，这样需要一个mapper对应一个factoryBean，不能动态的生成mapper的代理对象，需要手动创建。
   ```java
   public class FrankFactoryBean implements FactoryBean {
       private SqlSession sqlSession;
       // UserMapper,OrderMapper
       private Class mapperClass;
   
       /**
        *  传入需要代理的mapper接口的class，然后在getObject方法中让mybatis生成对应的mapper接口的代理对象
         */
       public FrankFactoryBean(Class mapperClass) {
           this.mapperClass = mapperClass;
       }
   
       /**
        * 从ioc容器中拿到factory 因为在AppConfig中直接Import的所以可以用到ioc容器，即使这个类不在包扫描范围内
        * @param factory
        */
       @Autowired
       public void setSqlSession(SqlSessionFactory factory) {
           factory.getConfiguration().addMapper(mapperClass);
           this.sqlSession = factory.openSession();
       }
   
       /**
        * mybatis生成的代理对象
        * @return
        * @throws Exception
        */
       @Override
       public Object getObject() throws Exception {
           // 让mybatis生成对应的mapper接口的代理对象
           Object mapper = sqlSession.getMapper(mapperClass);
           return mapper;
       }
   
       @Override
       public Class<?> getObjectType() {
           return mapperClass;
       }
   }
   ```
5. FrankScanner.java 继承了ClassPathBeanDefinitionScanner，我们需要得到@FrankMapperScan("com.lxc.mapper")写的包中的所有接口并让mybatis生成代理对象。
   ClassPathBeanDefinitionScanner这是Spring的扫描器，但是Spring会自动忽略接口类型，所以需要重写其中的方法。
   ```java
   public class FrankScanner extends ClassPathBeanDefinitionScanner {
       public FrankScanner(BeanDefinitionRegistry registry) {
           super(registry);
       }
   
       /**
        * 重写，只扫描接口
        * @param beanDefinition
        * @return
        */
       @Override
       protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
           return beanDefinition.getMetadata().isInterface();
       }
   
       /**
        * 重写doScan得到扫描到的 BeanDefinitionHolder
        * 并修改BeanDefination成自己的
        * @param basePackages
        * @return
        */
       @Override
       protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
           Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
           for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
               // 获取到BeanDefinition
               BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
               // 为BeanDefinition 加上构造方法的参数
               try {
                   beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(Class.forName(beanDefinition.getBeanClassName()));
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
               // BeanDefinition修改className等于 修改了整个BeanDefinition，
               // 最后对象的创建就是依靠BeanDefinition的class name，然后通过反射获得对象
               beanDefinition.setBeanClassName(FrankFactoryBean.class.getName());
           }
           return beanDefinitionHolders;
       }
   }
   ```
6. FrankImportBeanDefinitionRegister.java 核心类 实现了ImportBeanDefinitionRegistrar，可以向其中注册BeanDefinition，但是具体的注册操作放到了scanner中，因为我们将BeanDefinitionRegistry作为scanner的构造器的参数传给了scanner。在scanner扫描的过程中，就像ioc中注册了对应的BeanDefinition，但是我们在最后修改了注册进去的BeanDefinition(注册的BeanDefinition也随着发生变化)。
   ```java
   /**
    * 添加beanDefinition 到spring容器中
    */
   public class FrankImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
       /**
        *
        * @param importingClassMetadata 导入这个类上的所有注解信息
        * @param registry
        */
       @Override
       public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
           // 得到mapper的扫描路径
           Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(FrankMapperScan.class.getName());
           String path = (String) annotationAttributes.get("value");
   
           // 得到继承过来的 spring扫描器
           FrankScanner frankScanner = new FrankScanner(registry);
           frankScanner.addIncludeFilter(new TypeFilter() {
               @Override
               public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                   return true;
               }
           });
           frankScanner.scan(path);
       }
   }
   ```

