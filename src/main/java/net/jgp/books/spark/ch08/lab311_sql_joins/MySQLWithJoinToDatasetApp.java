package net.jgp.books.spark.ch08.lab311_sql_joins;

import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * MySQL injection to Spark, using the Sakila sample database.
 *
 * @author jgp
 */
public class MySQLWithJoinToDatasetApp {

    /**
     * main() is your entry point to the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        MySQLWithJoinToDatasetApp app =
                new MySQLWithJoinToDatasetApp();
        app.start();
    }

    /**
     * The processing code.
     */
    private void start() {
        SparkSession spark = SparkSession.builder()
                .appName(
                        "MySQL with join to Dataframe using JDBC")
                .master("local")
                .getOrCreate();

        // Using properties
        Properties props = new Properties();
        props.put("user", "root");
        props.put("password", "Spark<3Java");
        props.put("useSSL", "false");
        props.put("serverTimezone", "EST");

        // Builds the SQL query doing the join operation
        String sqlQuery =
                "select actor.actor_id, actor.first_name, actor.last_name, actor.last_update actor_last_update, film.* "
                        // instead of simply * which would repeat the actor_id and film_id columns
                        // of the film_actor join table and this doesn't work with creating a
                        // dataframe creating a
                        // java.sql.SQLSyntaxErrorException: Duplicate column name 'actor_id'
                        // also the last_update column is present in both remaining tables
                        // therefore a different alias is used for that column.
                        + "from actor, film_actor, film "
                        + "where actor.actor_id = film_actor.actor_id "
                        + "and film_actor.film_id = film.film_id";

        Dataset<Row> df = spark.read().jdbc(
                "jdbc:mysql://localhost:3306/sakila",
                "(" + sqlQuery + ") actor_film_alias",
                props);

        // Displays the dataframe and some of its metadata
        df.show(5);
        df.printSchema();
        System.out.println("The dataframe contains " + df
                .count() + " record(s).");
    }
}
