/*
 * Copyright since 2018 Nhut Do <mr.nhut@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.donhut.web.model;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public enum Themes {

    AFTER_DARK("afterdark"), AFTER_NOON("afternoon"), AFTER_WORK("afterwork"),
    ARISTO("aristo"), BLUE_TIE("black-tie"), BLITZER("blitzer"), BLUE_SKY("bluesky"),
    BOOTSTRAP("bootstrap"), CASABLANCA("casablanca"), CUPERTINO("cupertino"),
    CRUZE("cruze"), DARK_HIVE("dark-hive"), DELTA("delta"), DOT_LUV("dot-luv"),
    EGGPLANT("eggplant"), EXCITE_BIKE("excite-bike"), FLICK("flick"),
    GLASS_X("glass-x"), HOME("home"), HOT_SNEAKS("hot-sneaks"), HUMANITY("humanity"),
    LE_FROG("le-frog"), MIDNIGHT("midnight"), MINT_CHOC("mint-choc"),
    OVERCAST("overcast"), PEPPER_GRINDER("pepper-grinder"), REDMOND("redmond"),
    ROCKET("rocket"), SAM("sam"), SMOOTHNESS("smoothness"), SOUTH_STREET("south-street"),
    START("start"), SUNNY("sunny"), SWANKY_PURSE("swanky-purse"), TRONTASTIC("trontastic"),
    UI_DARKNESS("ui-darkness"), UI_LIGHTNESS("ui-lightness"), VADER("vader");

    private String name;

    Themes(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return StringUtils.capitalize(this.name);
    }
}
