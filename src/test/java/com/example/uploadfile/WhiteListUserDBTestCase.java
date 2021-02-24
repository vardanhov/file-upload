package com.example.uploadfile;

import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.service.WhiteListUserService;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dbunit.*;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WhiteListUserService.class})
//@TestExecutionListeners({
//        //TransactionalTestExecutionListener.class,
//       // DependencyInjectionTestExecutionListener.class,
//        DbUnitTestExecutionListener.class
//})
public class WhiteListUserDBTestCase extends AbstractDatabaseTester  {

    @Autowired
    private WhiteListUserService whiteListUserService;

    @MockBean
    private WhiteListUserRepository whiteListUserRepository;

    @MockBean
    private UserRepository userRepository;

    private IDatabaseTester databaseTester;

    @Before
    public void setUp() throws Exception {
        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:sample", "sa", "","upload");
//        Connection jdbcConnection = DriverManager.getConnection(
//                "jdbc:hsqldb:sample", "sa", "");
        IDatabaseConnection connection = databaseTester.getConnection();
      //  InputStream dataStream = getClass().getResourceAsStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml");
      //  IDataSet initialDataSet = new FlatXmlDataSet(dataStream);
          IDataSet initialDataSet = new FlatXmlDataSetBuilder()
               .build(new FileInputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml"));      // IDataSet fullDataSet = connection.createDataSet();
        //FlatXmlDataSet.write(fullDataSet, new FileOutputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml"));
       DatabaseOperation.CLEAN_INSERT.execute(connection, initialDataSet);
        databaseTester.setDataSet( new FlatXmlDataSetBuilder().build(new File("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml")));
        databaseTester.setOperationListener(new DefaultOperationListener());

        //Set up before and after test behaviour
//        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
//        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();
    }
//
    public WhiteListUserDBTestCase() throws FileNotFoundException, DataSetException {

        this.setDataSet(new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml")));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "org.hsqldb.jdbcDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:hsqldb:sample");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
                "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
                "");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
                "upload");
    }

//    @Override
//    protected IDatabaseConnection getConnection() throws Exception {
//        Class.forName("org.hsqldb.jdbcDriver");
//        Connection jdbcConnection =
//                DriverManager.getConnection("jdbc:hsqldb:sample",
//                        "sa", "");
//        return new DatabaseConnection(jdbcConnection);
//    }


//    public DatabaseOperation getSetUpOperation()  {
//        return DatabaseOperation.CLEAN_INSERT;
//    }
//
//
//    public DatabaseOperation getTearDownOperation() {
//        return DatabaseOperation.NONE;
//    }

//    @Override
//    protected IDataSet getDataSet() throws Exception {
//        return new CachedDataSet(new FlatXmlProducer(new InputSource("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml")));
////                new FlatXmlDataSetBuilder()
////                .build(new FileInputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml"));
//    }

    @Test
    @DatabaseSetup("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml")
    public void updateUserAccessTest() throws Exception {
        //whiteListUserRepository.changePermissions();
        whiteListUserService.updateUserAccess();


       // IDataSet dataSet = new FlatXmlDataSetBuilder()
       //         .build(new File("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml"));
        //IDataSet dataSet = getConnection().createDataSet();
        IDataSet dataSet =  this.getConnection().createDataSet();
                //new CachedDataSet(new FlatXmlProducer(new InputSource("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml")));
        ITable actualTable = dataSet.getTable("WHITE_LIST_USER");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(new FileInputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet_Expected.xml"));
        ITable expectedTable = expectedDataSet.getTable("WHITE_LIST_USER");

        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
        Assertion.assertEquals(expectedTable, filteredActualTable);
    }


    @Override
    public IDatabaseConnection getConnection() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        Connection jdbcConnection =
                DriverManager.getConnection("jdbc:hsqldb:sample",
                        "sa", "");
        return new DatabaseConnection(jdbcConnection);
    }
}
