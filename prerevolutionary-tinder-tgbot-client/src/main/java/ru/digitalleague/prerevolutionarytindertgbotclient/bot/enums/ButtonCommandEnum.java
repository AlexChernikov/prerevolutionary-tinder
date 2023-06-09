package ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums;

public enum ButtonCommandEnum {

    MALE {
        public String getDescription() {
            return "/sudar";
        }
    },
    FEMALE {
        public String getDescription() {
            return "/sudarinya";
        }
    },
    ABOUT {
        public String getDescription() {
            return "/about";
        }
    },
    ALL_SEARCH {
        public String getDescription() {
            return "/all_person_search";
        }
    },
    MALE_SEARCH {
        public String getDescription() {
            return "/sudar_search";
        }
    },
    FEMALE_SEARCH {
        public String getDescription() {
            return "/sudarinya_search";
        }
    },
    ACCOUNT {
        public String getDescription() {
            return "/account";
        }
    },
    SEARCH {
        public String getDescription() {
            return "/search";
        }
    },
    LIKE {
        public String getDescription() {
            return "/like";
        }
    },
    DISLIKE {
        public String getDescription() {
            return "/dislike";
        }
    },
    FAVORITES {
        public String getDescription() {
            return "/favorites";
        }
    };

    public String getDescription() {
        return this.getDescription();
    }
}
