package com.ivan.MoviesRDF.rest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Movie;
import com.ivan.MoviesRDF.service.CompanyFilter;
import com.ivan.MoviesRDF.service.MovieFilter;
import com.ivan.MoviesRDF.service.MovieService;
import com.ivan.MoviesRDF.service.SparkRepository;

import org.apache.commons.io.IOUtils;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    // String resultsDefault = "100";
    // String orderBy = "numberMovies";
    // Boolean asc;

    @Autowired
    MovieService movieService;

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/movie")
    public String movies(Model model, HttpServletRequest request, @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer sortType, @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer clear, @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String genre, @RequestParam(required = false) String prod,
            @RequestParam(required = false) String country, @RequestParam(required = false) Boolean asc) {
        HttpSession session = request.getSession();

        if (clear != null && clear == 1) {
            session.removeAttribute("movieasc");
            session.removeAttribute("movielimit");
            session.removeAttribute("moviesortType");
            session.removeAttribute("moviesearch");
        }

        // session
        Boolean ascSession = (Boolean) session.getAttribute("movieasc");
        Integer limitSession = (Integer) session.getAttribute("movielimit");
        Integer sortTypeSession = (Integer) session.getAttribute("moviesortType");
        String searchSession = (String) session.getAttribute("moviesearch");

        // defaults
        if (ascSession == null)
            ascSession = false;
        if (limitSession == null)
            limitSession = 100;
        if (sortTypeSession == null)
            sortTypeSession = 3;

        // query
        if (search != null)
            searchSession = search;
        if (limit != null)
            limitSession = limit;
        if (sortType != null) {
            sortTypeSession = sortType;
            session.setAttribute("movieasc", !ascSession);
        }
        if (asc != null) {
            ascSession = asc;
            session.setAttribute("movieasc", false);
        }

        session.setAttribute("movielimit", limitSession);
        session.setAttribute("moviesortType", sortTypeSession);
        session.setAttribute("moviesearch", searchSession);

        model.addAttribute("movielist",
                MovieFilter.getFilter(movieService.getMovieList()).filterGenre(genre).filterCompany(prod)
                        .filterCountry(country).filter(searchSession).order(sortTypeSession, ascSession)
                        .limit(limitSession).get());

        if (movieId != null) {
            model.addAttribute("selectedMovie", movieService.getMovie(movieId));
        }

        return "movie";
    }

    @GetMapping(value = "/production")
    public String productions(Model model, HttpServletRequest request, @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer sortType, @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer clear) {

        HttpSession session = request.getSession();

        if (clear != null && clear == 1) {
            session.removeAttribute("asc");
            session.removeAttribute("limit");
            session.removeAttribute("sortType");
            session.removeAttribute("search");
        }

        // session
        Boolean ascSession = (Boolean) session.getAttribute("asc");
        Integer limitSession = (Integer) session.getAttribute("limit");
        Integer sortTypeSession = (Integer) session.getAttribute("sortType");
        String searchSession = (String) session.getAttribute("search");

        // defaults
        if (ascSession == null)
            ascSession = false;
        if (limitSession == null)
            limitSession = 100;
        if (sortTypeSession == null)
            sortTypeSession = 3;

        // query
        if (search != null)
            searchSession = search;
        if (limit != null)
            limitSession = limit;
        if (sortType != null) {
            sortTypeSession = sortType;
            session.setAttribute("asc", !ascSession);
        }

        session.setAttribute("limit", limitSession);
        session.setAttribute("sortType", sortTypeSession);
        session.setAttribute("search", searchSession);

        model.addAttribute("companylist", CompanyFilter.getFilter(movieService.getCompanyList()).filter(searchSession)
                .order(sortTypeSession, ascSession).limit(limitSession).get());

        return "productions";
    }

    @GetMapping(value = "/about")
    public String about() {

        return "about";
    }

    @GetMapping(value = "/test")
    public String tset() {
        Long start = System.currentTimeMillis();

        List<Movie> list2 = movieService.getMovieList();
        Long list2finish = System.currentTimeMillis();

        list2.stream().forEach(System.out::println);
        System.out.println(list2.size() + "first finish in :" + (list2finish - start));
        return "about";
    }

    @GetMapping(value = "/genre")
    public String genres(Model model) {
        model.addAttribute("genrelist", movieService.getGenreList().stream()
                .sorted(Comparator.comparing(Genre::getNumberMovies).reversed()).collect(Collectors.toList()));
        return "genres";
    }

    @ResponseBody
    @GetMapping(value = "/data", produces = { "text/turtle" })
    public ResponseEntity<?> data() throws IOException {
        InputStream fileStream = MainController.class.getResourceAsStream("/movies.ttl");
        BufferedInputStream bfi = new BufferedInputStream(fileStream);
        byte[] bytes = IOUtils.toByteArray(bfi);

        fileStream.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/icon", produces = { "image/png" })
    public ResponseEntity<?> icon(@RequestParam String icon) throws IOException {
        InputStream fileStream = MainController.class
                .getResourceAsStream("/static/images/" + icon.replace(" ", "") + ".png");
        BufferedInputStream bfi = new BufferedInputStream(fileStream);
        byte[] bytes = IOUtils.toByteArray(bfi);

        bfi.close();
        fileStream.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

    @Autowired
    SparkSession sparkSession;

    @GetMapping(value = "/testeroni3")
    @ResponseBody
    public List<CastMember> tse12213t() throws AnalysisException {

        List<CastMember> cast = movieService.getCastMembers(8967L);
        Dataset<Row> dataFrame = sparkSession.createDataFrame(cast, CastMember.class);
        dataFrame.show();
        dataFrame.createOrReplaceTempView("cast");
        dataFrame.write().mode(SaveMode.Overwrite).save("C:\\Users\\Duck\\Desktop\\genres.parquet");

        // List<Movie> movies = movieService.getMovieList();
        // Encoder<Movie> personEncoder = Encoders.bean(Movie.class);
        // Dataset<Movie> dataFramemovies = sparkSession.createDataset(movies,
        // personEncoder);
        // dataFramemovies.show();
        // dataFramemovies.createOrReplaceTempView("movie");
        // dataFramemovies.write().mode(SaveMode.Append).save("C:\\Users\\Duck\\Desktop\\genres.parquet");

        // Dataset<Row> dataset = sparkSession.sql("SELECT * FROM genre");
        // List<Row> rows = dataset.collectAsList();

        // return rows.stream().map(row -> {
        // return new Genre(row.getString(0), row.getInt(1));
        // }).collect(Collectors.toList());
        return null;

    }

    @GetMapping(value = "/testeroni")
    @ResponseBody
    public List<Company> tse12t() throws AnalysisException {

        List<Company> genres = movieService.getCompanyList();
        Dataset<Row> dataset = sparkSession.createDataFrame(genres, Company.class);
        // sparkSession.table(tableName)
        dataset.show();
        // dataFrame.createTempView("genre");
        // / dataFrame.createGlobalTempView("genre");
        dataset.write().mode(SaveMode.Append).option("path", "C:\\Users\\Duck\\Desktop\\testpni\\company.parquet")
                .saveAsTable("genre");
        // File file = new File("C:\\Users\\Duck\\Desktop\\testpni\\genres.parquet");
        // file.delete();
        // dataFrame.write().save("C:\\Users\\Duck\\Desktop\\testpni\\genres.parquet");

        // =======================

        List<Row> rows = sparkSession.sql("SELECT * FROM genre").collectAsList();

        return rows.stream().map(row -> {
            return new Company(row.getString(0), row.getInt(1), row.getLong(2));
        }).collect(Collectors.toList());

        // StructType structType = dataFrame.schema();

        // RelationalGroupedDataset groupedDataset = dataFrame.groupBy(new
        // Column("name"));

        // List<Row> rows = groupedDataset.count().collectAsList();//
        // JavaConversions.asScalaBuffer(words)).count();
        // List<Row> rows2 = dataFrame.collectAsList();
        // List<Row> rows3 = JavaConversions.asScalaBuffer(genres).count();
        // groupedDataset.count().show();

        // return rows.stream().map(new Function<Row, Genre>() {
        // @Override
        // public Genre apply(Row row) {
        // return new Genre(row.getString(0), row.getInt(1));
        // }
        // }).collect(Collectors.toList());
        // return null;

    }

    @Autowired
    SparkRepository sparkRepo;

    @GetMapping(value = "/testeroni2")
    @ResponseBody
    public String tse122t() {

        sparkRepo.getGenreList();

        return "gotem";

        // // sparkSession.sparkContext().addFile(path, minPartitions)
        // // sparkSession.catalog().listTables().collectAsList();
        // Dataset<Row> dataset =
        // sparkSession.read().parquet("C:\\Users\\Duck\\Desktop\\testpni\\" + type +
        // ".parquet");
        // // Dataset<Row> dataset = sparkSession.sql("SELECT * FROM genre");
        // // sparkSession.sparkContext().
        // // Dataset<Row> dataset = sparkSession.read;

        // List<Row> rows = dataset.collectAsList();

        // if (type.equals("genres")) {
        // return rows.stream().map(row -> {

        // return new Genre(row.getString(0), row.getInt(1));
        // }).collect(Collectors.toList());
        // } else {
        // return rows.stream().map(row -> {
        // return new Company(row.getString(0), row.getInt(1), row.getLong(2));
        // }).collect(Collectors.toList());
        // }

    }
}