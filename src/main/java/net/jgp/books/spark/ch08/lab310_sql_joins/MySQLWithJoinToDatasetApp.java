package net.jgp.books.spark.ch08.lab310_sql_joins;

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

        // Builds the SQL query doing a theta join operation
        String thetaJoinSyntax =
                "select actor.first_name, actor.last_name, film.title,"
                        + " film.description"
                        + " from actor, film_actor, film"
                        + " where actor.actor_id = film_actor.actor_id"
                        + " and film_actor.film_id = film.film_id"
                        + " and actor.last_name = 'WINSLET'"
                        + " and film.rating = 'NC-17'";

        // inner join syntax with the same result
        String joinSyntaxQuery =
                "select actor.first_name, actor.last_name, film.title, "
                + "film.description"
                + " from actor"
                + " inner join film_actor"
                + " on actor.actor_id = film_actor.actor_id"
                + " inner join film"
                + " on film_actor.film_id = film.film_id"
                + " where actor.last_name = 'WINSLET'"
                + " and film.rating = 'NC-17'";

        Dataset<Row> df = spark.read().jdbc(
                "jdbc:mysql://localhost:3306/sakila",
                //when you use a select query as table parameter it must be surrounded with parentheses followed by an alias
                "(" + joinSyntaxQuery + ") actor_film_alias",
                props);

        // Displays the dataframe and some of its metadata
        df.show(12, 80);
        df.printSchema();
        System.out.println("The dataframe contains " + df
                .count() + " record(s).");
    }
}
