package cn.uway.smc.db.conn.bdb;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.util.SysCfg;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityJoin;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.ForwardCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;

/**
 * BerkeleyDB 数据库基本操作
 * 
 * @author liuwx
 * @since 1.0
 * @see Environment,Database,EnvironmentConfig ,Transaction,DatabaseEntry
 */
public class BerkeleyDBUtils {

	private final static Logger LOG = LoggerFactory
			.getLogger(BerkeleyDBUtils.class);

	private static final String DB_ENVIRONMENT_ROOT = SysCfg.getInstance()
			.getBerkeleyDbRootPath();

	private static Environment dbEnvironment = null;//

	public static Database db = null;

	private static String ENCODE = "UTF-8";

	private static String db_name = "sampleDatabase1";

	static {
		createDBEnvironment();
		createDB();
	}

	/**
	 * 打开database环境
	 * 
	 * @return
	 */
	public static void createDBEnvironment() {
		if (dbEnvironment != null) {
			return;
		}
		try {
			EnvironmentConfig config = new EnvironmentConfig();
			config.setAllowCreate(true);// 如果不存在则创建一个
			config.setReadOnly(false); // 以只读方式打开，默认为false
			config.setTransactional(true); // 事务支持,如果为true，则表示当前环境支持事务处理，默认为false，不支持事务处理

			dbEnvironment = new Environment(new File(DB_ENVIRONMENT_ROOT),
					config);
		} catch (DatabaseException e) {
			LOG.error("创建berkeley db 环境失败 ，原因：{}", e);
		}
	}

	/**
	 * RAM cache命中率
	 * 
	 * @return
	 */
	public long getRAMStatus() {
		long status = 0;
		try {
			// 来监视RAM cache命中率
			status = dbEnvironment.getStats(null).getNCacheMiss();
		} catch (DatabaseException e) {
			LOG.error("获取berkeley db RAM cache命中率 失败 ，原因：{}", e);
		}
		return status;
	}

	/**
	 * 关闭database环境
	 */
	public static void closeDBEnvironment() {
		try {
			if (dbEnvironment != null) {
				// dbEnvironment.cleanLog(); // 在关闭环境前清理下日志
				dbEnvironment.close();
			}
		} catch (DatabaseException e) {
			LOG.error(" 关闭berkeley database环境失败 ，原因：{}", e);
		}
	}

	/**
	 * 创建一个数据库
	 */
	public static void createDB() {
		// 打开一个数据库，数据库名为
		if (db != null) {
			return;
		}
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		try {
			db = dbEnvironment.openDatabase(null, db_name, dbConfig);

		} catch (DatabaseException e) {
			LOG.error(" 创建一个berkeley db 数据库环境失败 ，原因：{}", e);
		}
	}

	public static void close() {
		closeDB();
		closeDBEnvironment();
	}

	/**
	 * 关闭数据库
	 */
	public static void closeDB() {
		try {
			if (db != null) {
				db.close();
			}
		} catch (DatabaseException e) {

			LOG.error("Error closing store: " + db.toString());
		}
	}

	public static EntityStore getEntityStore(String storeName) {
		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setTransactional(true);

		EntityStore store = new EntityStore(dbEnvironment, storeName,
				storeConfig);

		return store;
	}

	/**
	 * 添加一条数据
	 * 
	 * @param txn
	 * @param key
	 * @param data
	 * @throws DatabaseException
	 */
	public void add(Transaction txn, DatabaseEntry key, DatabaseEntry data)
			throws DatabaseException {
		db.put(txn, key, data);
	}

	/**
	 * 删除一条数据
	 * 
	 * @param txn
	 * @param key
	 */
	public void delete(Transaction txn, DatabaseEntry key) {
		// 删除数据
		db.delete(txn, key);
	}

	/**
	 * 查询数据
	 * 
	 * @param key
	 * @param data
	 * @param encode
	 * @throws UnsupportedEncodingException
	 *             @
	 */
	public void query(DatabaseEntry key, DatabaseEntry data, String encode)
			throws UnsupportedEncodingException {
		if (db.get(null, key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			byte[] retData = data.getData();
			String foundKey = new String(key.getData(), encode);
			String foundData = new String(retData, encode);
			LOG.debug("For key: '" + foundKey + "' found data: '" + foundData
					+ "'.");

		}
	}

	/**
	 * 查询数据
	 * 
	 * @param key
	 * @param data
	 * @throws UnsupportedEncodingException
	 */
	public static void query(DatabaseEntry key, DatabaseEntry data)
			throws UnsupportedEncodingException {
		if (db.get(null, key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			byte[] retData = data.getData();
			String foundKey = new String(key.getData(), ENCODE);
			String foundData = new String(retData, ENCODE);
			LOG.debug("For key: '" + foundKey + "' found data: '" + foundData
					+ "'.");

		}
	}

	/**
	 * 重命名数据库
	 */
	public void renameDatabase(String newDbName) {
		try {

			if (db != null) {
				// 必须先关闭数据库
				db.close();
				dbEnvironment.renameDatabase(null, db_name, newDbName);// 重命名,必须先关闭数据库
			}

			if (dbEnvironment != null) {
				dbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			LOG.error("重新命名 berkeley db name 失败,原因:{}", dbe);
		}
	}

	/**
	 * 重命名数据库
	 */
	public void renameDatabase(String oldDbName, String newDbName) {
		try {

			if (db != null) {
				// 必须先关闭数据库
				db.close();
				dbEnvironment.renameDatabase(null, oldDbName, newDbName);// 重命名,必须先关闭数据库
			}
			if (dbEnvironment != null) {
				dbEnvironment.close();
			}

		} catch (DatabaseException dbe) {
			LOG.error("重新命名 berkeley db name 失败,原因:{}", dbe);
		}
	}

	/**
	 * 删除数据库
	 */
	public void removeDatabase() {
		try {
			if (db != null) {
				// 必须先关闭数据库
				db.close();
				dbEnvironment.removeDatabase(null, db_name);// 删除数据库,必须先关闭数据库
			}
			if (dbEnvironment != null) {
				dbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			LOG.error("删除 berkeley db  失败,原因:{}", dbe);
		}
	}

	/**
	 * 删除数据库
	 */
	public void removeDatabase(String dbName) {
		try {
			if (db != null) {
				// 必须先关闭数据库
				db.close();
				dbEnvironment.removeDatabase(null, dbName);// 删除数据库,必须先关闭数据库
			}
			if (dbEnvironment != null) {
				dbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			LOG.error("删除 berkeley db  失败,原因:{}", dbe);
		}
	}

	/**
	 * truncate数据库
	 */
	public void truncateDatabase() {
		try {
			if (db != null) {
				// 必须先关闭数据库
				db.close();
				dbEnvironment
						.truncateDatabase(null, db.getDatabaseName(), true);// 删除并回收数据库空间
																			// ，true返回删除的记录的数量,false不返回删除的记录数量值
			}
			if (dbEnvironment != null) {
				dbEnvironment.close();
			}

		} catch (DatabaseException dbe) {
			LOG.error("truncate berkeley db  失败,原因:{}", dbe);
		}
	}

	/**
	 * truncate数据库
	 */
	public void truncateDatabase(String dbName) {
		try {
			if (db != null) {
				// 必须先关闭数据库
				db.close();
				dbEnvironment.truncateDatabase(null, dbName, true);// 删除并回收数据库空间
			}
			if (dbEnvironment != null) {
				dbEnvironment.close();
			}

		} catch (DatabaseException dbe) {
			LOG.error("truncate berkeley db  失败,原因:{}", dbe);
		}
	}

	public static Environment getDbEnvironment() {
		return dbEnvironment;
	}

	/**
	 * Do prefix query, similar to the SQL statement: <blockquote>
	 * 
	 * <pre>
	 * SELECT * FROM table WHERE col LIKE 'prefix%';
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param index
	 * @param prefix
	 * @return
	 * @throws DatabaseException
	 */
	public <V> EntityCursor<V> doPrefixQuery(EntityIndex<String, V> index,
			String prefix) throws DatabaseException {

		assert (index != null);
		assert (prefix.length() > 0);

		/* Opens a cursor for traversing entities in a key range. */
		char[] ca = prefix.toCharArray();
		final int lastCharIndex = ca.length - 1;
		ca[lastCharIndex]++;
		return doRangeQuery(index, prefix, true, String.valueOf(ca), false);
	}

	/**
	 * Do range query, similar to the SQL statement: <blockquote>
	 * 
	 * <pre>
	 * SELECT * FROM table WHERE col >= fromKey AND col <= toKey;
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param index
	 * @param fromKey
	 * @param fromInclusive
	 * @param toKey
	 * @param toInclusive
	 * @return
	 * @throws DatabaseException
	 */
	public <K, V> EntityCursor<V> doRangeQuery(EntityIndex<K, V> index,
			K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
			throws DatabaseException {

		assert (index != null);

		/* Opens a cursor for traversing entities in a key range. */
		return index.entities(fromKey, fromInclusive, toKey, toInclusive);
	}

	/**
	 * Do a "AND" join on a single primary database, similar to the SQL:
	 * <blockquote>
	 * 
	 * <pre>
	 * SELECT * FROM table WHERE col1 = key1 AND col2 = key2;
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param pk
	 * @param sk1
	 * @param key1
	 * @param sk2
	 * @param key2
	 * @return
	 * @throws DatabaseException
	 */
	public <PK, SK1, SK2, E> ForwardCursor<E> doTwoConditionsJoin(
			PrimaryIndex<PK, E> pk, SecondaryIndex<SK1, PK, E> sk1, SK1 key1,
			SecondaryIndex<SK2, PK, E> sk2, SK2 key2) throws DatabaseException {

		assert (pk != null);
		assert (sk1 != null);
		assert (sk2 != null);

		EntityJoin<PK, E> join = new EntityJoin<PK, E>(pk);
		join.addCondition(sk1, key1);
		join.addCondition(sk2, key2);

		return join.entities();
	}

	public static void main(String[] args) {
		BerkeleyDBUtils.getEntityStore("SMCCfgSys");
		dbEnvironment.removeDatabase(null, "SMCCfgSys");
		BerkeleyDBUtils.closeDB();
	}

}