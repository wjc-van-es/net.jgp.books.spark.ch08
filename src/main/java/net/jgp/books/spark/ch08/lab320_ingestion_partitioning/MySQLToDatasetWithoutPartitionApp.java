package net.jgp.books.spark.ch08.lab320_ingestion_partitioning;

import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * MySQL injection to Spark, using the Sakila sample database.
 *
 * @author jgp
 */
public class MySQLToDatasetWithoutPartitionApp {

    /**
     * main() is your entry point to the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        MySQLToDatasetWithoutPartitionApp app =
                new MySQLToDatasetWithoutPartitionApp();
        app.start();
    }

    /**
     * The processing code.
     */
    private void start() {
        SparkSession spark = SparkSession.builder()
                .appName(
                        "MySQL to Dataframe using JDBC without partitioning")
                .master("local")
                .getOrCreate();

        // Using properties
        Properties props = new Properties();
        props.put("user", "root");
        props.put("password", "Spark<3Java");
        props.put("useSSL", "false");
        props.put("serverTimezone", "EST");

        // Used for partitioning
//        props.put("partitionColumn", "fid");
//        props.put("lowerBound", "1");
//        props.put("upperBound", "1000");
//        props.put("numPartitions", "1");

        // Let's look for all movies matching the query
        Dataset<Row> df = spark.read().jdbc(
                "jdbc:mysql://localhost:3306/sakila",
                "nicer_but_slower_film_list", //actually a view, but a select query substitute is also valid
                props);

        // Displays the dataframe and some of its metadata
        df.show(5, 80);
        df.printSchema();
        System.out.println("The dataframe contains " + df
                .count() + " record(s).");
        System.out.println("The dataframe is split over " + df.rdd()
                .getPartitions().length + " partition(s).");
    }
}
