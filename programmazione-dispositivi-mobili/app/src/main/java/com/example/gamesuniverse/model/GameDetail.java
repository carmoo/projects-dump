package com.example.gamesuniverse.model;

import java.util.Arrays;
import java.util.List;

public class GameDetail {
    private int id;
    private String slug;
    private String name;
    private String description;
    private int metacritic;
    private String released;
    private boolean tba;
    private String updated;
    private String background_image;
    private String background_image_additional;
    private String website;
    private double rating;
    private int added;
    private int playtime;
    private int creatorsCount;
    private int achievementsCount;
    private String redditUrl;
    private String redditName;
    private String redditDescription;
    private String redditLogo;
    private int redditCount;
    private String twitchCount;
    private String youtubeCount;
    private int ratingsCount;
    private String[] alternativeNames;
    private List<Platform> platforms;
    private List<Genre> genres;
    private List<Publisher> publishers;

    public GameDetail() {
    }

    public GameDetail(int id, String slug, String name, String description, int metacritic, String released, boolean tba,
                      String updated, String background_image, String background_image_additional, String website, double rating,
                      int added, int playtime, int creatorsCount, int achievementsCount, String redditUrl, String redditName,
                      String redditDescription, String redditLogo, int redditCount, String twitchCount, String youtubeCount,
                      int ratingsCount, String[] alternativeNames, List<Platform> platforms, List<Genre> genres, List<Publisher> publishers) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.metacritic = metacritic;
        this.released = released;
        this.tba = tba;
        this.updated = updated;
        this.background_image = background_image;
        this.background_image_additional = background_image_additional;
        this.website = website;
        this.rating = rating;
        this.added = added;
        this.playtime = playtime;
        this.creatorsCount = creatorsCount;
        this.achievementsCount = achievementsCount;
        this.redditUrl = redditUrl;
        this.redditName = redditName;
        this.redditDescription = redditDescription;
        this.redditLogo = redditLogo;
        this.redditCount = redditCount;
        this.twitchCount = twitchCount;
        this.youtubeCount = youtubeCount;
        this.ratingsCount = ratingsCount;
        this.alternativeNames = alternativeNames;
        this.platforms = platforms;
        this.genres = genres;
        this.publishers = publishers;
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public String getReleased() {
        return released;
    }

    public boolean isTba() {
        return tba;
    }

    public String getUpdated() {
        return updated;
    }

    public String getBackground_image() {
        return background_image;
    }

    public String getBackground_image_additional() {
        return background_image_additional;
    }

    public String getWebsite() {
        return website;
    }

    public double getRating() {
        return rating;
    }

    public int getAdded() {
        return added;
    }

    public int getPlaytime() {
        return playtime;
    }

    public int getCreatorsCount() {
        return creatorsCount;
    }

    public int getAchievementsCount() {
        return achievementsCount;
    }

    public String getRedditUrl() {
        return redditUrl;
    }

    public String getRedditName() {
        return redditName;
    }

    public String getRedditDescription() {
        return redditDescription;
    }

    public String getRedditLogo() {
        return redditLogo;
    }

    public int getRedditCount() {
        return redditCount;
    }

    public String getTwitchCount() {
        return twitchCount;
    }

    public String getYoutubeCount() {
        return youtubeCount;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public String[] getAlternativeNames() {
        return alternativeNames;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setTba(boolean tba) {
        this.tba = tba;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public void setBackground_image_additional(String background_image_additional) {
        this.background_image_additional = background_image_additional;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public void setCreatorsCount(int creatorsCount) {
        this.creatorsCount = creatorsCount;
    }

    public void setAchievementsCount(int achievementsCount) {
        this.achievementsCount = achievementsCount;
    }

    public void setRedditUrl(String redditUrl) {
        this.redditUrl = redditUrl;
    }

    public void setRedditName(String redditName) {
        this.redditName = redditName;
    }

    public void setRedditDescription(String redditDescription) {
        this.redditDescription = redditDescription;
    }

    public void setRedditLogo(String redditLogo) {
        this.redditLogo = redditLogo;
    }

    public void setRedditCount(int redditCount) {
        this.redditCount = redditCount;
    }

    public void setTwitchCount(String twitchCount) {
        this.twitchCount = twitchCount;
    }

    public void setYoutubeCount(String youtubeCount) {
        this.youtubeCount = youtubeCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public void setAlternativeNames(String[] alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }

    @Override
    public String toString() {
        return "GameDetail{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", metacritic=" + metacritic +
                ", released='" + released + '\'' +
                ", tba=" + tba +
                ", updated='" + updated + '\'' +
                ", background_image='" + background_image + '\'' +
                ", background_image_additional='" + background_image_additional + '\'' +
                ", website='" + website + '\'' +
                ", rating=" + rating +
                ", added=" + added +
                ", playtime=" + playtime +
                ", creatorsCount=" + creatorsCount +
                ", achievementsCount=" + achievementsCount +
                ", redditUrl='" + redditUrl + '\'' +
                ", redditName='" + redditName + '\'' +
                ", redditDescription='" + redditDescription + '\'' +
                ", redditLogo='" + redditLogo + '\'' +
                ", redditCount=" + redditCount +
                ", twitchCount='" + twitchCount + '\'' +
                ", youtubeCount='" + youtubeCount + '\'' +
                ", ratingsCount=" + ratingsCount +
                ", alternativeNames=" + Arrays.toString(alternativeNames) +
                ", platforms=" + platforms +
                ", genres=" + genres +
                ", publishers=" + publishers +
                '}';
    }

    public class Platform {
        private Data platform;

        public Platform() {
        }

        public Platform(Data platform) {
            this.platform = platform;
        }

        public Data getPlatform() {
            return platform;
        }

        public void setPlatform(Data platform) {
            this.platform = platform;
        }

        @Override
        public String toString() {
            return platform.toString();
        }

        public class Data {
            private int id;
            private String name;

            public Data() {
            }

            public Data(int id, String name) {
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Data{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

    }

    public class Publisher {
        private int id;
        private String name;

        public Publisher() {
        }

        public Publisher(int id, String name) {
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Publisher{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
