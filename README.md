# t-sql-common
> 这是一个半自动的DTO到sql的转换工具,将项目中的DTO转换为可执行的JDBC,并不完全依赖实体的创建  
> 警告,默认单个JDBC的执行时间为10秒,如果超过10秒直接会超时,可以使用此方法进行修改  
```java
Session session = sf.getCurrentSession();
Query<Map<String,Object>> q = session.createQuery("select * from test where id=:id",HashMap.class);
//超时时间调整,此方法用来调整JDBC的超时时间
q.setTimeOut(20);
q.setParameter("id", "4b0621afba0844c79b02072e9f1e990e");
List<Map<String,Object>> datas =q.list();
```

## 快速开始
##### 1. 创建DTO实体对象
```java
@Table
public class Test implements DTO{
	@Id
	@Column(name="id")
	private String id;
	@Column(name="name")
	private String name;
	@Column(name="value")
	private String value;
	@Column(name="commen")
	private String commen;
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCommen() {
		return commen;
	}
	public void setCommen(String commen) {
		this.commen = commen;
	}
}
```
以上就是一个简单的DTO实体对象,目前实体的注解信息如下 ,注意需要实现DTO的接口,这样可以表明这是一个DTO对象,此接口不需要实现任何方法 

|注解名称                                                                  |说明
|-----                   |-----
|javax.persistence.Table |如果不指定name表示使用class的名称来操作对应数据库的表,否则可以使用name属性来进行设置
|javax.persistence.Id    |用来指定数据库的主键,通常在数据库中组件应该是一个唯一的GUID或者UUID,自增的,不支持联合主键
|javax.persistence.Column|用来指定列的名称以及相关的属性,如果不指定name属性,那么默认为字段的名称  

##### 2. 进行保存操作

```java
test.t.sql.dto.Test t = new test.t.sql.dto.Test();
t.setId("id");
t.setName("name");
t.setValue("value");
t.setCommen("commen");
sf.getCurrentSession().create(t);

//完整的代码可以在test.t.sql.TestSession这个类里面查看,并进行测试
/** 测试的表名称为test ,表结构如下
id                               |name |value |commen |
---------------------------------|-----|------|-------|
18d684a581dc4fd3a38bdadecc981661 |name |value |123    |
**/
```


