package net.jgp.books.spark.ch08.lab100_mysql_ingestion;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * MySQL injection to Spark, using the Sakila sample database, without using
 * properties
 * 
 * @author jgp
 */
public class MySQLToDatasetWithOptionsApp {

  /**
   * main() is your entry point to the application.
   * 
   * @param args
   */
  public static void main(String[] args) {
    MySQLToDatasetWithOptionsApp app =
        new MySQLToDatasetWithOptionsApp();
    app.start();
  }

  /**
   * The processing code.
   */
  private void start() {
    SparkSession spark = SparkSession.builder()
        .appName("MySQL to Dataframe using a JDBC Connection")
        .master("local")
        .getOrCreate();

    // In a "one-liner" with method chaining and options
    Dataset<Row> df = spark.read()
        .option("url", "jdbc:mysql://localhost:3306/sakila")
//        .option("dbtable", "actor")
        .option("dbtable", "nicer_but_slower_film_list") // this is actually a view not a table, but it works as well
        .option("user", "root")
        .option("password", "Spark<3Java")
        .option("useSSL", "false")
        .option("serverTimezone", "EST")
        .format("jdbc")
        .load();
    // the nicer_but_slower_film_list has no last_name column (use title column instead)
    //df = df.orderBy(df.col("last_name"));
    df = df.orderBy(df.col("category"), df.col("title") );

    // Displays the dataframe and some of its metadata
    df.show(5, 80);
    df.printSchema();
    System.out.println("The dataframe contains "
        + df.count()
        + " record(s).");
  }
}
