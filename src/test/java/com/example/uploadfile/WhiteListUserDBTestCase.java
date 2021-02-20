package com.example.uploadfile;

import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.service.WhiteListUserService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.dbunit.*;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
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
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WhiteListUserService.class})
public class WhiteListUserDBTestCase extends DBTestCase {

    @Autowired
    private WhiteListUserService whiteListUserService;

    @MockBean
    private WhiteListUserRepository whiteListUserRepository;

    @MockBean
    private UserRepository userRepository;

    private IDatabaseTester databaseTester;

    @Before
    public void setUp() throws Exception {
       // databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver",
       //         "jdbc:hsqldb:sample", "sa", "sa","upload");
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:hsqldb:sample", "sa", "");
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        IDataSet dataSet = getDataSet();
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
       // databaseTester.setDataSet( dataSet );
       // databaseTester.onSetup();
    }

    public WhiteListUserDBTestCase(){

        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "org.hsqldb.jdbcDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:hsqldb:sample");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
                "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
                "");
//        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
//                "upload");
    }

//    @Override
//    protected DatabaseOperation getSetUpOperation() throws Exception {
//        return DatabaseOperation.CLEAN_INSERT;
//    }
//
//    @Override
//    protected DatabaseOperation getTearDownOperation() throws Exception {
//        return DatabaseOperation.NONE;
//    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder()
                .build(new FileInputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml"));
    }

    @Test
    public void updateUserAccessTest() throws Exception {
       // IDataSet exDataSet = getDataSet();

        whiteListUserRepository.changePermissions();
        //whiteListUserService.updateUserAccess();


       // IDataSet dataSet = new FlatXmlDataSetBuilder()
       //         .build(new File("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet.xml"));
        IDataSet dataSet = getConnection().createDataSet();
        ITable actualTable = dataSet.getTable("WHITE_LIST_USER");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(new FileInputStream("src/test/java/com/example/uploadfile/data/WhiteListUserTestDataSet_Expected.xml"));
        ITable expectedTable = expectedDataSet.getTable("WHITE_LIST_USER");

        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
        Assertion.assertEquals(expectedTable, filteredActualTable);
    }


}
