package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.GenreStatistics;
import com.karankumar.bookproject.backend.statistics.PageStatistics;
import com.karankumar.bookproject.backend.statistics.RatingStatistics;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.java.Log;

import java.text.DecimalFormat;

@Route(value = "statistics", layout = MainView.class)
@PageTitle("Statistics | Book Project")
@Log
public class StatisticsView extends VerticalLayout {
    public StatisticsView(PredefinedShelfService predefinedShelfService) {
        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
        GenreStatistics genreStatistics = new GenreStatistics(predefinedShelfService);
        PageStatistics pageStatistics = new PageStatistics(predefinedShelfService);

        Double averageRating = ratingStatistics.calculateAverageRatingGiven();
        if (averageRating != null) {
            String averageRatingOutOf10 = String.format("%s/10", new DecimalFormat("#.00").format(averageRating));
            add(configureStatistic("Average rating given", averageRatingOutOf10));
        }

        Book mostLikedBook = ratingStatistics.findMostLikedBook();
        if (mostLikedBook != null) {
            String mostLikedBookStatistic =
                    formatStatistic(mostLikedBook.getTitle(), mostLikedBook.getRating().toString(), "rating");
            add(configureStatistic("Most liked book", mostLikedBookStatistic));
        }

        Book leastLikedBook = ratingStatistics.findLeastLikedBook();
        if (leastLikedBook != null) {
            String leastLikedBookRating =
                    formatStatistic(leastLikedBook.getTitle(), leastLikedBook.getRating().toString(), "rating");
            add(configureStatistic("Least liked book", leastLikedBookRating));
        }

        Genre mostReadGenre = genreStatistics.findMostReadGenre();
        if (mostReadGenre != null) {
            add(configureStatistic("Most read genre", mostReadGenre.toString()));
        }

        Genre mostLikedGenre = genreStatistics.findMostLikedGenre();
        if (mostLikedGenre != null) {
            add(configureStatistic("Most liked genre read", mostLikedGenre.toString()));
        }

        Genre leastLikedGenre = genreStatistics.findLeastLikedGenre();
        if (leastLikedGenre != null) {
            add(configureStatistic("Least liked genre read", leastLikedGenre.toString()));
        }

        Integer averagePageLength = pageStatistics.calculateAveragePageLength();
        if (averagePageLength != null) {
            add(configureStatistic("Average page length", String.format("%d pages", averagePageLength)));
        }

        Book longestBook = pageStatistics.findBookWithMostPages();
        if (longestBook != null) {
            String longestBookStatistic =
                    formatStatistic(longestBook.getTitle(), String.valueOf(longestBook.getNumberOfPages()), "pages");
            add(configureStatistic("Longest book read", longestBookStatistic));
        }

        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private Div configureStatistic(String caption, String statistic) {
        Div div = new Div();
        H3 averageRatingGiven = new H3(caption + ":");
        VerticalLayout verticalLayout = new VerticalLayout(averageRatingGiven, new Text(statistic));
        verticalLayout.setAlignItems(Alignment.CENTER);
        div.add(verticalLayout);
        return div;
    }

    private String formatStatistic(String bookTitle, String statistic, String unit) {
        return String.format("%s (%s %s)", bookTitle, statistic, unit);
    }
}