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

## 快速开始  (注意： 当前只支持在同一个线中进行事务控制，不支持跨线程事务的处理)
##### 1. 创建DTO实体对象   

以下就是一个简单的DTO实体对象,目前实体的注解信息如下 ,注意需要实现DTO的接口,这样可以表明这是一个DTO对象,此接口不需要实现任何方法 

|注解名称                                                                  |说明
|-----                   |-----
|javax.persistence.Table |如果不指定name表示使用class的名称来操作对应数据库的表,否则可以使用name属性来进行设置
|javax.persistence.Id    |用来指定数据库的主键,通常在数据库中组件应该是一个唯一的GUID或者UUID,自增的,不支持联合主键
|javax.persistence.Column|用来指定列的名称以及相关的属性,如果不指定name属性,那么默认为字段的名称  

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

##### 2. 进行保存操作
完整的代码可以在test.t.sql.TestSession这个类里面查看,并进行测试,表test结构如下    
``` 
id                               |name |value |commen |
---------------------------------|-----|------|-------|
18d684a581dc4fd3a38bdadecc981661 |name |value |123    | 
```

```java
test.t.sql.dto.Test t = new test.t.sql.dto.Test();
t.setId("id");
t.setName("name");
t.setValue("value");
t.setCommen("commen");
sf.getCurrentSession().create(t);
```
##### 3. 进行更新操作
```java
test.t.sql.dto.Test t = new test.t.sql.dto.Test();
t.setId("id");
t.setName("name");
t.setValue("value");
t.setCommen("commen");
//此方法用来保存一个最新的数据
sf.getCurrentSession().create(t); 
t.setCommen("test");
//此方法有来测试数据库是否正常更新
sf.getCurrentSession().update(t);
```   

update 是根据DTO的ID字段来进行更新,这个工具类不会校验数据库的表结构,如果不是主键,可能会导致同一个实体更新多条数据,很显然这并不是想要达到的效果  

##### 4. 更新删除操作  

```java
test.t.sql.dto.Test t = new test.t.sql.dto.Test();
t.setId("id");
t.setName("name");
t.setValue("value");
t.setCommen("commen");
//保存一个数据,进行删除测试
sf.getCurrentSession().create(t);
//删除这个数据
sf.getCurrentSession().delete(t);
```
update 是根据DTO的ID字段来进行删除,这个工具类不会校验数据库的表结构,如果不是主键,可能会导致同一个实体删除多条数据,很显然这并不是想要达到的效果  

##### 5. 删除操作  

```java
test.t.sql.dto.Test t = new test.t.sql.dto.Test();
t.setId("id");
t.setName("name");
t.setValue("value");
t.setCommen("commen");
//创建数据用于测试删除
sf.getCurrentSession().create(t);
t.setCommen("test");
//删除数据
sf.getCurrentSession().delete(t);
```  
delete 是根据DTO的id字段来进行操作   

##### 6. 查询数据
```java
Session session = sf.getCurrentSession();
Query<Map<String,Object>> q = session.createQuery("select * from test where id=:id",HashMap.class);
//设置数据库超时时间,如果是查询数据,设置超时时间很有必要,可以在早期就可以发现出性能比较差的sql
q.setTimeOut(20);
q.setParameter("id", "4b0621afba0844c79b02072e9f1e990e");
List<Map<String,Object>> datas =q.list();
System.out.println(datas.size());
```


##### 6. 对原生SQL的DML支持
```java
Session session = sf.getCurrentSession();
String sql = "update test where id=? ";
//执行原生SQL的DML操作
int i = session.nativeDMLSQL(sql,"12345678");
System.out.println(i);
```
如果需要对原生SQL进行DML的操作支持,那么使用nativeDMLSQL即可,主要参数替换需要采用**?**号的方式进行操作 

##### 7. 对in查询的支持  

```java
Query<Map<String,Object>> query =sessionFactory.getCurrentSession().createQuery("select * from user_tab_comments where table_name in :table_name", HashMap.class);
List<Object> d  = new ArrayList<Object>();
d.add("YW_XJZL");
d.add("YW_XJZL_0524");
query.setListParameter("table_name", d);
List<Map<String, Object>> result = query.list();
System.out.println(result.size());
```
如果需要查询使用in那么采用setListParameter即可进行in查询的支持

##### 8. 使用ListUtils.toLowerCaseMapKey 进行对List结果中的Map的key进行转换

```java
List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
Map<String,Object> m = new HashMap<String,Object>();
m.put("ABCD","123");
m.put(null,"1234");
list.add(m);
System.out.println(t.sql.utils.ListUtils.toLowerCaseMapKey(list));
```
在ListUtils中提供了对List操作方便的工具类,有兴趣的可以看看此类的方法

