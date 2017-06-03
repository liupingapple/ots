import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils;

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // 'net.sf.ehcache.hibernate.EhCacheRegionFactory' WARNING: Your cache provider is set to 'net.sf.ehcache.hibernate.EhCacheRegionFactory' in DataSource.groovy, however the class for this provider cannot be found.
}

// environment specific settings, prefer to use conf/spring/resources.xml to define the datasource

// MySQL settings

dataSource {
	pooled = true
	driverClassName = "com.mysql.jdbc.Driver"
	dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
}

environments {
	development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
			
			// need to update datasource4test.properties when local mysql changed for development
			def props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource4test.properties"))
			pooled = true
			driverClassName = props.getProperty("driverClassName")
			dialect = props.getProperty("dialect")
			username = props.getProperty("username")
			password = props.getProperty("password")
			url = props.getProperty("url")
			
			println "url: ${url}"
			println "username/password: ${username}/${password}"
			
			properties {
				// http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html#Common_Attributes
			   // 初始化N个连接
			   initialSize = props.getProperty("prop.initialSize")
			   
			   // 最大活动连接数，即最大并发数，阿里云RDB上显示是支持60个，注意目前mysql最大支持连接数是110（show variables like '%max_connections%'）
			   maxActive = props.getProperty("prop.maxActive")
			   
			   // 最小空闲连接数
			   minIdle = props.getProperty("prop.minIdle")
			   
			   // 建立连接时最大等待时间
			   maxWait = props.getProperty("prop.maxWait")
			   // maxAge = 10 * 60000
			   
			   // 每隔timeBetweenEvictionRunsMillis检查超过空闲minEvictableIdleTimeMillis的连接，注意Tomcat默认设置的session超时时间为30分钟
			   timeBetweenEvictionRunsMillis = props.getProperty("prop.timeBetweenEvictionRunsMillis")
			   minEvictableIdleTimeMillis = props.getProperty("prop.minEvictableIdleTimeMillis")
			}
			
			//logSql = true
        }
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/otsdb?useUnicode=yes&characterEncoding=UTF-8"
			username = "ots"
			password = "welcomeots"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			// DB conf file is in home dir
			String confFile = System.getProperty("user.home")+File.separator+"ots.properties"
			Properties props = new Properties()
			props.load(new FileReader(confFile))
			url = props.getProperty("DB_URL") // "jdbc:mysql://10.168.84.46:3306/otsdb?useUnicode=yes&characterEncoding=UTF-8"
			username = props.getProperty("DB_USER") // "ots"
			password = props.getProperty("DB_PASS") // "welcomeots"
			
			println "production DB: ${url}, ${username}/${password}"
			
			properties {
				// http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html#Common_Attributes
			   // 初始化N个连接
			   initialSize = 3
			   // 每隔timeBetweenEvictionRunsMillis检查超过空闲minEvictableIdleTimeMillis的连接，注意Tomcat默认设置的session超时时间为30分钟
			   timeBetweenEvictionRunsMillis = 600000
			   minEvictableIdleTimeMillis = 600000
			   testOnBorrow = true
			   validationQuery = "select 1"
			}
		}
	}
}

/**
 * 
------------------ DataBase and user init --------
mysql> drop database otsdb;
mysql> CREATE DATABASE `otsdb` DEFAULT CHARACTER SET utf8  COLLATE utf8_general_ci;
mysql> grant all on otsdb.* to ots identified by "welcomeots";

----------- WeiXinUtil ots.properties init 

root@iZ238h4di3tZ:~# cat ots.properties
WEIXIN_NAME=OTSV2
APP_ID=wxcd1adcc9dd893ce8
APP_SECRET=b078c8f00ec4f354ca1148ff9fcf522d
WEIXIN_PM=gh_33818a38fbfb
ADMIN_OPENID=oU_kgs4ssZ6GnIeH6m6kpJ9C8AdU
WEB_HOME=http://121.40.200.161

DB_URL=jdbc:mysql://10.168.84.46:3306/otsdb?useUnicode=yes&characterEncoding=UTF-8
DB_USER=ots
DB_PASS=welcomeots

 */

// H2 settings

/*dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}

environments {
	 development {
		 dataSource {
			 dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
			 url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
		 }
	 }
	 test {
		 dataSource {
			 dbCreate = "update"
			 url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
		 }
	 }
	 production {
		 dataSource {
			 dbCreate = "update"
			 url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
		 }
	 }
}*/

// Postgress settings


/*dataSource {
	pooled = true
    driverClassName = "org.postgresql.Driver"
	dialect = org.hibernate.dialect.PostgreSQLDialect
}

environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			uri = new URI(System.env.DATABASE_URL?:"postgres://test:test@localhost/test")		   
			url = "jdbc:postgresql://"+uri.host+uri.path
			username = uri.userInfo.split(":")[0]
			password = uri.userInfo.split(":")[1]
			
//          driverClassName = "org.postgresql.Driver"
//			dialect = org.hibernate.dialect.PostgreSQLDialect
//			uri = new URI(System.env.DATABASE_URL?:"postgres://test:test@localhost/test")		   
//			url = "jdbc:postgresql://"+uri.host+uri.path
//			username = uri.userInfo.split(":")[0]
//			password = uri.userInfo.split(":")[1]
			
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop"
			uri = new URI(System.env.DATABASE_URL?:"postgres://test:test@localhost/test")		   
			url = "jdbc:postgresql://"+uri.host+uri.path
			username = uri.userInfo.split(":")[0]
			password = uri.userInfo.split(":")[1]
        }
    }
    production {
        dataSource {
            dbCreate = "create-drop"
			uri = new URI(System.env.DATABASE_URL?:"postgres://test:test@localhost/test")		   
			url = "jdbc:postgresql://"+uri.host+uri.path
			username = uri.userInfo.split(":")[0]
			password = uri.userInfo.split(":")[1]
        }
    }
}*/

