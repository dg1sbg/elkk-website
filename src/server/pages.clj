(ns server.pages
  (:require [server.db-api :as db-api]
            [datomic.api :as d]
            [clj-time.coerce :as c]
            [clj-time.core :as t]))

(defn header [db]
 [:div
  [:div
   {:class "leadline flexbox"}
   [:div {:class "flex-1 background-black"}]

   [:div {:class "flex-1 background-red"}]

   [:div {:class "flex-1 background-green"}]]

  [:div
   {:class "container"}

   [:div
    {:class "row"}

    [:div
     {:class
      "flexbox col-xs-12 col-sm-12 col-md-6 col-lg-6 logo-name header-row"}

     [:img
      {:src "resources/fill_400x400_logo_eldoret_ohne_schrift.png",
       :class "logo"}]

     [:span {:class "flex-1"} "Eldoret Kids Kenia e.V. "]]

    [:div
     {:class
      "col-lg-6 col-md-6 header-right col-xs-12 col-sm-12 header-row flexbox"}
     [:a {:href (str "rundbriefe/" (db-api/get-current-rundbrief-name db))
          :class "flex-1 rundbrief-span flexbox"}
       [:img
        {:src "resources/iconmonstr-note-19-240.png", :class "header-right-img"}]
       [:span
        {:class "quicklink-span flex-1"}
        "Rundbrief"]]
     [:a {:href "mailto:eldoret-kids@t-online.de"
          :class "flex-1 flexbox"}
       [:img
        {:src "resources/iconmonstr-email-12-240.png",
         :width "20",
         :height "20",
         :class "header-right-img"}]
       [:span
        {:class "quicklink-span flex-1"}
        "eldoret-kids@t-online.de"]]]]
   [:div
    {:class "col-md-12 col-xs-12"}

    [:nav
     {:class "row nav"}

     [:a
      {:class "nav-element col-xs-3 col-sm-3 col-md-3 col-lg-3"
       :href "/"}
       ;:on-click (fn [evt] (accountant/navigate! "/"))}

      [:span {} "Start"]]

     [:a
      {:class "nav-element col-lg-3 col-md-3 col-sm-3 col-xs-3"
       :href "/verein"}
       ;:on-click (fn [evt] (accountant/navigate! "/verein"))}
      [:span {} "Verein"]]

     [:a
      {:class "nav-element col-sm-3 col-xs-3 col-md-3 col-lg-3"
       :href "/projekt"}
       ;:on-click (fn [evt] (accountant/navigate! "/projekt"))}
      [:span {} "Projekt"]]

     [:a
      {:class "nav-element col-xs-3 col-sm-3 col-md-3 col-lg-3"
       :href "/unterstuetzung"}
       ;:on-click (fn [evt] (accountant/navigate! "/unterstuetzung"))}
      [:span {} "Unterstützung"]]]]]])

(defn home [db]
 [:div
  [:div
   {:class "container section"}
   [:div
    {:class "img-section"}
    [:img {:src "resources/title.png", :class "img"}]
    [:div
     {:class "slogan-outer"}
     [:p
      {:class "slogan"}
      "Badilisha Maisha Centre"
      [:br {}]
      [:span {:class "slogan-sub"} "Verändere Leben"]]
     [:span
      {:class "slogan-text"}
      "Wir unterstützen Straßenkinder in Kenia"
      [:br {}]
      "auf ihrem Weg zurück in ein"
      [:br {}]
      "geordnetes Leben."
      [:br {}]
      [:a {:href "/projekt"}
        [:button {:class "button"} "Mehr erfahren"]]]]]]
  [:div
   {:class "container"}

   [:div
    {:class "row"}
    (let [events (db-api/get-event db)]
      (if (not (empty? events))
        [:div
         {:class "col-xs-12 col-sm-12 col-md-6 col-lg-6 section"}
         (doall (for [event events]
                 [:div
                  {:class "row"}
                  [:div
                   {:class
                    "flexbox icon-heading col-xs-10 col-sm-10 col-md-10 col-lg-10"}
                   [:div
                    {:class "section-icon evt-icon"}
                    [:div {:class "evt-day"} (t/day (c/from-date (:event/instant event)))]]
                   [:div
                    {:class "flex-1"}
                    [:p {:class "section-heading"} "Am " (t/day (c/from-date (:event/instant event))) ". "(["Januar" "Februar" "März" "April" "Mai" "Juni" "Juli" "August" "September" "Oktober" "November" "Dezember"] (- (t/month (c/from-date (:event/instant event))) 1))
                        " in " (:event/location event) "."]
                    [:p
                     {:class "section-heading-bold evt-heading"}
                     (:event/title event)]]]
                  [:div
                   {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12"}
                   [:span
                    {}
                    (:event/description event)]]]))]))
    [:div
     {:class "col-xs-12 col-sm-12 col-md-6 col-lg-6 section"}
     [:div
      {:class "row"}
      [:div
       {:class
        "flexbox icon-heading col-lg-10 col-md-10 col-sm-10 col-xs-10"}
       [:div {:class "section-icon good-icon"}]
       [:div
        {:class "flex-1"}
        [:p {:class "section-heading"} "Wir sind"]
        [:p
         {:class "section-heading-bold normal-section-heading"}
         "Gut für den Landkreis Esslingen"]]]
      [:div
       {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12"}
       [:span
        {}
        "Wir sind als Projekt auf der Aktionsseite gut-fuer-den-landkreis-esslingen.de vertreten, welche von Betterplace.org gemeinsam mit der Kreissparkasse Esslingen ins Leben gerufen wurde. Über die Aktionsseite kannst Du uns auch per Onlinespende untersützen."
        [:br {}]
        [:a {:href "http://www.gut-fuer-den-landkreis-esslingen.de/projects/35529"}
          [:button {:class "button"} "Zur Projektseite"]]]]]]
    [:div
     {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
     [:div
      {:class "row"}
      [:div
       {:class
        "flexbox icon-heading col-md-10 col-sm-10 col-xs-10 col-lg-10"}
       [:div {:class "section-icon news-icon"}]
       [:div
        {:class "flex-1"}
        [:p
         {:class "section-heading"}
         "Verein und Badilisha Maisha Centre"]
        [:p
         {:class "section-heading-bold normal-section-heading"}
         "Neues"]]]
      [:div
       {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12"}
       (doall (for [news (db-api/get-news db)]
               [:article
                {:class "news"}
                [:p
                 {:class "news-heading"}
                 (:news/title news)]
                [:span
                 {}
                 (:news/body news)]]))]]]]]])

(defn org [db]
 [:div
   [:div
    {:class "container section"}
    [:div
      {:class "img-section"}
      [:img {:src "resources/PastedGraphicPNG.png", :class "img"}]]]
   [:div
    {:class "container"}
    [:div
     {:class "row"}
     [:div
      {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11 section"}
      [:p
       {:class
        "section-heading-bold normal-section-heading page-heading"}
       "Eldoret Kids Kenia e.V."]]
     [:div
      {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
      [:span
       {}
       "Am 11.09.2013 in Bempflingen gegründet, unterstützt der Eldoret Kids Kenia e.V. die Arbeit mit Straßenkindern in Eldoret/Kenia, vor allem die Arbeit im Rehabilitationszentrum \"Badilisha Maisha Centre\"."
       [:br {}]
       "In seinen Ausschüssen vermittelt und koordiniert er Patenschaften für Kinder vor Ort."]
      [:br {}]
      [:a {:href "/unterstuetzung#patenschaft"}
        [:button {:class "button"} "Weitere Infos zur Patenschaft"]]]
     [:div
      {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
      [:span
       {}
       "Der Verein ist Ansprechpartner für Freiwillige, die im Projekt mitarbeiten möchten und organisiert Veranstaltungen zugunsten des Projekts. Neue Mitglieder sind immer herzlich willkommen."]
      [:br {}]
      [:a {:href "/unterstuetzung#mitarbeit"}
        [:button {:class "button"} "Weitere Infos zur Mitarbeit"]]
      [:a {:href "/unterstuetzung#mitgliedschaft"}
        [:button {:class "button"} "Weitere Infos zur Mitgliedschaft"]]
      [:div
       {:class "row"}
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        [:p
         {:class
          "section-heading-bold normal-section-heading page-heading section"}
         "Unser Vorstand"]]
       [:div
        {:class "col-md-4 vorstand col-lg-4 col-sm-4 col-xs-12"}
        [:img {:src "resources/holgerdembek.jpg"}]
        [:p
         {:class "vorstand-name"}
         [:b {} "Holger Dembek"]
         [:br {}]
         [:i {} "1. Vorsitzender"]]]
       [:div
        {:class "col-md-4 vorstand col-lg-4 col-sm-4 col-xs-12"}
        [:img {:src "resources/ingridspeth.png"}]
        [:p
         {:class "vorstand-name"}
         [:b {} "Ingrid Speth"]
         [:br {}]
         [:i {} "2. Vorsitzende"]]]
       [:div
        {:class "col-md-4 vorstand col-lg-4 col-sm-4 col-xs-12"}
        [:img {:src "resources/anettegratwohl.png"}]
        [:p
         {:class "vorstand-name"}
         [:b {} "Anette Gratwohl"]
         [:br {}]
         [:i {} "3. Vorsitzende" [:br {}] "Kassiererin"]]]]
      [:div
       {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
       [:div
        {:class "row"}
        [:div
         {:class
          "flexbox icon-heading col-md-10 col-sm-10 col-xs-10 col-lg-10"}
         [:div {:class "section-icon news-icon"}]
         [:div
          {:class "flex-1"}
          [:p {:class "section-heading"} "Vom Verein"]
          [:p
           {:class "section-heading-bold normal-section-heading"}
           "Neues"]]]
        [:div
         {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12"}
         (doall (for [news (db-api/get-news db "elkk")]
                 [:article
                  {:class "news"}
                  [:p
                   {:class "news-heading"}
                   (:news/title news)]
                  [:span
                   {}
                   (:news/body news)]]))]]]]]]])

(defn project [db]
  [:div
   [:div
     {:class "container section"}
     [:div
       {:class "img-section"}
       [:img {:src "resources/P1040025.JPG", :class "img"}]]]
   [:div
    {:class "container"}
    [:div
     {:class "row"}
     [:div
      {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
      [:div
       {:class "row"}
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        [:p
         {:class "section-heading-bold page-heading"}
         "Straßenkinder"]]
       [:div
        {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
        [:span
         {}
         "Straßenkinder gehören in Kenias Städten zum Alltag. Ihre Zahl stieg in den letzten Jahrzehnten stetig an."
         [:br {}]
         [:br {}]
         "\nHeute gibt es etwa 300.000 Straßenkinder in Kenia. Armut, Arbeitslosigkeit und schlechte Bezahlung - oft reicht das Geld nicht zum Überleben der Familien - veranlasst viele Kinder in Kenia dazu, ihre Familie zu verlassen. Soziale Probleme wie der Alkoholismus der Männer, der zu seelischem und körperlichem Missbrauch von Kindern führt, und überforderte alleinerziehende Mütter veranlassen vor allem Jungen dazu, in den Städten Kenias Zuflucht und Schutz zu suchen. Auch AIDS ist ein Grund dafür, dass die Zahl der Straßenkinder stetig wächst."
         [:br {}]
         [:br {}]
         "\nIn Eldoret, der viertgrößten Stadt Kenias, haben rund 75% der Straßenkinder Erfahrungen mit Drogen. Am beliebtesten ist neben Alkohol, Zigaretten und Marihuana vor allem das Schnüffeln von Klebstoffen. Kleber ist billig, verfügbar und legal erwerbbar."
         [:br {}]
         [:br {}]
         "\nDie Kinder inhalieren den Klebstoff, um ihrem bitteren Alltag auf Kenias Straßen zu entfliehen. Nachts schnüffeln sie, um der Kälte zu trotzen."
         [:br {}]
         [:br {}]
         "\nAußerdem werden viele Kinder kriminell. Sie betteln und stehlen, um zu überleben. Für viele von ihnen ist das Leben auf der Straße ein täglicher Kampf ums Überleben."]
        [:br {}]]]
      [:div
       {:class "row"}
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        [:p
         {:class "section-heading-bold page-heading evt-heading"}
         "Birgit Zimmermann"]]
       [:div
        {:class "text-block col-lg-8 col-md-8 col-sm-8 col-xs-8"}
        [:span
         {}
         "Aufgrund der ständig wachsenden Zahl der Straßenkinder und ihrer scheinbar ausweglosen Situation, bei der ein Leben in Armut vorprogrammiert ist, hat Birgit Zimmermann aus Bempflingen vor 9 Jahren begonnen, in Kenia mit Straßenkindern zu arbeiten."
         [:br {}]
         [:br {}]
         "\nBirgit Zimmermann, 46 Jahre alt, ist ausgebildete Erzieherin. Sie arbeitete mehrere Jahre in Kindergärten in Bempflingen, Metzingen, Grafenberg und Dettingen/Teck, bevor sie während eines Kurz-Missionseinsatzes mit Jugend Mit Einer Mission (JMEM) in Eldoret/Kenia ihre Bestimmung für die Arbeit mit Straßenkindern entdeckt hat."
         [:br {}]
         [:br {}]
         "\nBirgit Zimmermann, die bereits 7 Jahre im Projekt der Keniahilfe Schwäbische Alb im Karai Vocational Center mitgearbeitet hat, wird auch zukünftig die ehemaligen Straßenkinder dieses Projektes, die nach Eldoret zurückgekehrt sind, betreuen."
         [:br {}]
         [:br {}]
         "\nBirgit wurde von JMEM als Missionarin nach Kenia entsandt und wird durch ihren Freundeskreis finanziert. Der neu gegründete Verein Eldoret Kids Kenia e.V. hat sich zur Aufgabe gemacht, Birgit bei ihrem Projekt Badilisha Maisha Centre finanziell und mit Rat und Tat zu unterstützen."
         [:br {}]
         [:br {}]
         "\nDer Verein stellt durch die Anwesenheit von Brigit vor Ort eine direkte und gezielte Verwendung seiner Spenden und eine kompetente Anleitung der Freiwilligen, die vor Ort mitarbeiten möchten, sicher."]
        [:br {}]]
       [:div
        {:class "text-block col-lg-4 col-md-4 col-sm-4 col-xs-4"}
        [:img {:src "resources/bizi.png", :class "img"}]]
       [:div
        {:class "container section"}
        [:div
         {:class "img-section"}
         [:img {:src "resources/bmc.jpg", :class "img"}]]]
       [:div
        {:class "container"}
        [:div
         {:class "row"}
         [:div
          {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
          [:div
           {:class "row"}
           [:div
            {:class
             "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
            [:p
             {:class
              "section-heading-bold page-heading normal-section-heading"}
             "Badilisha Maisha Centre"]]
           [:div
            {:class
             "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
            [:span
             {}
             "Das Badilisha Maisha Centre nimmt Kinder auf, die durch widrige Umstände auf der Straße leben müssen. In der Einrichtung lernen sie, ohne Drogenkonsum zu leben und wieder einem geregelten Tagesablauf nachzugehen."
             [:br {}]
             [:br {}]
             "\nEine für die Kinder passende Schule wird gesucht, Schuluniform und Bücher gekauft und auf den regelmäßigen Besuch der Schule geachtet. Sie lernen außerdem mit den Sorgen und Ängsten, die sie in der Zeit als Straßenkind verfolgten, umzugehen."
             [:br {}]
             [:br {}]
             "\nZudem wird durch die Aufgaben des täglichen Lebens, wie Putzen, Wäsche waschen und der Mithilfe auf einem kleinen Bauernhof die Reintegration in die (Rest-) Familie vorbereitet."
             [:br {}]
             [:br {}]
             "\nDie Kinder sollen durch christlich geprägte Anleitung, Gesprächskreise und Jugendgottesdienste erkennen, dass Gott ihr Leben verändern möchte."
             [:br {}]
             [:br {}]
             "\nDas Hauptziel des Projektes ist, dass die Kinder während ihrer Zeit im Badilisha Maisha Centre wieder Kontakt zu ihren Familien aufnehmen. Viele der Straßenkinder haben noch Familienangehörige, sei es Mutter, Vater, Tante, Onkel oder Oma. Manche von ihnen haben sogar noch eine \"komplette\" Familie, die aber aus finanziellen Gründen nicht alle Kinder ernähren konnte."
             [:br {}]
             [:br {}]
             "\nDie Kinder können nach erfolgreicher Rehabilitierung, wenn sie wieder den normalen Lebensrhythmus verinnerlicht haben, in ihre Familien zurückkehren. Während der Zeit im Centre wird untersucht, wie man der Familie helfen kann, ihr Kind wieder aufzunehmen. Meist reicht es aus für das Schulgeld und die Lebensmittel aufzukommen, damit die Familie wieder zusammenleben kann."
             [:br {}]
             [:br {}]
             "\nAuch wenn die Kinder wieder in ihre (Rest-)Familien zurückgekehrt sind, werden sie von Birgit Zimmermann und ihren Sozialarbeitern weiterhin angeleitet.\nWichtig ist auch, dass die Familien nicht durch direkte finanzielle Hilfe unterstützt werden, um etwaigem Missbrauch des Geldes vorzubeugen.\nDie Unterstützung erfolgt durch Bezahlung des Schulgeldes oder der Ausbildung direkt in der Schule oder dem Betrieb und durch Sachzuwendungen (Kleidung, Schuhe, Schuluniform, Schulbücher, Lebensmittel...).\nUm möglichst vielen Kindern die Rückkehr in ein geregeltes Familienleben ermöglichen zu können, vermitteln wir Patenschaften. Durch eine Vermittlerstelle in Deutschland können zwischen Paten und Patenkindern auch Briefe, Zeichnungen, usw. ausgetauscht werden."]
            [:br {}]
            [:a {:href "/unterstuetzung#patenschaft"}
              [:button {:class "button"} "Weitere Infos zur Patenschaft"]]]]]]]]
      [:div
       {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
       [:div
        {:class "row"}
        [:div
         {:class
          "flexbox icon-heading col-md-10 col-sm-10 col-xs-10 col-lg-10"}
         [:div {:class "section-icon news-icon"} "\n"]
         [:div
          {:class "flex-1"}
          [:p {:class "section-heading"} "Badilisha Maisha Centre"]
          [:p
           {:class "section-heading-bold normal-section-heading"}
           "Neues"]]]
        [:div
         {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12"}
         (doall (for [news (db-api/get-news db "bmc")]
                 [:article
                  {:class "news"}
                  [:p
                   {:class "news-heading"}
                   (:news/title news)]
                  [:span
                   {}
                   (:news/body news)]]))]]]]]]])


(defn support [db]
 [:div
   [:div
      {:class "container section"}
      "\n            "
      [:div
       {:class "img-section"}
       "\n                "
       [:img {:src "resources/support.png", :class "img"}]
       "\n            "]
      "\n        "
     "\n        "]
   [:div
    {:class "container"}
    "\n            "
    [:div
     {:class "row"}
     "\n                "
     [:div
      {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
      "\n                    "
      [:div
       {:class "row"}
       "\n                        "
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        "\n                            "
        [:a
         {:class
          "section-heading-bold page-heading normal-section-heading"
          :name "spenden"}
         "Spenden"]
        " \n                        "]
       "\n                        "
       [:div
        {:class "text-block col-xs-12 col-sm-12 col-md-12 col-lg-12"}
        "\n                            "
        [:div
         {:class "context-info"}
         "\n                                "
         [:p {:class "context-info-heading"} [:b {} "Spendenkonto"]]
         "\n                                "
         [:span
          {}
          "Eldoret Kids Kenia e.V."
          [:br {}]
          "\nKreissparkasse Esslingen"
          [:br {}]
          [:b {} "IBAN "]
          " DE10 6115 0020 0101 9812 93"
          [:br {}]
          [:b {} "BIC "]
          " ESSLDE66XXX"]
         "\n                            "]
        "\n                            "
        [:span
         {}
         "Ihre Spende kommt auf direktem Wege in voller Höhe dem Projekt zugute. Spenden können Sie durch eine Überweisung des gewünschten Betrags auf unser Spendenkonto."
         [:br {}]
         [:br {}]
         [:b {} "Online Spenden"]
         [:br {}]
         [:br {}]
         "\nEs gibt außerdem die Möglichkeit, online zu spenden. Betterplace.org ist eine Online-Spendenplattform für gemeinnützige Projekte und übernimmt für uns die Spendenabwicklung."
         [:br {}]
         [:br {}]
         [:button {:class "button"} "Zum Spendenformular"]
         [:br {}]
         [:br {}]
         "\nFalls Sie per Überweisung spenden, erhalten Sie am Ende des Jahres eine Spendenbescheinigung zugesandt.\nVermerken Sie deshalb bitte Ihre Adresse im Feld \"Bemerkungen\" auf dem Überweisungsformular.\nBei einer Online-Spende erhalten Sie Ihre Spendenbescheinigung von betterplace.org."]
        "\n                            "
        [:br {}]
        " \n                        "]
       "\n                    "]
      "\n                    "
      [:div
       {:class "row"}
       "\n                        "
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        "\n                            "
        [:a
         {:class
          "section-heading-bold page-heading normal-section-heading"
          :name "pagenschaft"}
         "Patenschaft"]
        " \n                        "]
       "\n                        "
       [:div
        {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
        "\n                            "
        [:div
         {:class "context-info"}
         "\n                                "
         [:p {:class "context-info-heading"} [:b {} "Kontaktadresse"]]
         "\n                                "
         [:span
          {}
          "Eldoret Kids Kenia e.V."
          [:br {}]
          "\nNeckartenzlinger Str. 9"
          [:br {}]
          "\n72658 Bempflingen"
          [:br {}]
          "\nDeutschland"]
         "\n                            "]
        "\n                            "
        [:span
         {}
         "Ein wichtiger Grundpfeiler unseres Projektes sind Patenschaften, die es den Straßenkindern ermöglichen, in ihre Familie zurückzukehren."
         [:br {}]
         "\nDurch Ihren Beitrag kann die Versorgung des Kindes sichergestellt werden."
         [:br {}]
         [:br {}]
         "\nWenn Sie gerne die Patenschaft für ein Kind vor Ort übernehmen möchten, füllen Sie bitte den Patenschaftsantrag aus und senden ihn an die angegebene Kontaktadresse."]
        "\n                            "
        [:br {}]
        "\n                            "
        [:button {:class "button"} "Patenschaftsantrag"]
        "                             \n                        "]
       "\n                    "]
      "\n                    "
      [:div
       {:class "row"}
       "\n                        "
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        "\n                            "
        [:a
         {:class
          "section-heading-bold page-heading normal-section-heading"
          :name "mitgliedschaft"}
         "Mitgliedschaft"]
        " \n                        "]
       "\n                        "
       [:div
        {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
        "\n                            "
        [:div
         {:class "context-info"}
         "\n                                "
         [:p {:class "context-info-heading"} [:b {} "Kontaktadresse"]]
         "\n                                "
         [:span
          {}
          "Eldoret Kids Kenia e.V."
          [:br {}]
          "\nNeckartenzlinger Str. 9"
          [:br {}]
          "\n72658 Bempflingen"
          [:br {}]
          "\n    Deutschland"]
         "\n                                "
         [:br {}]
         "\n                                "
         [:br {}]
         " \n                                "
         [:p
          {:class "context-info-heading"}
          [:b {} "Mitgliedsbeiträge"]]
         "\n                                "
         [:span
          {}
          "Erwachsene: 20 Euro"
          [:br {}]
          "\nFamilien: 30 Euro"
          [:br {}]
          "\n    Schüler und Studenten: 10 Euro"]
         "\n                            "]
        "\n                            "
        [:span
         {}
         "Auch Ihre Mitgliedschaft im Verein unterstützt die Arbeit in Eldoret/Kenia."
         [:br {}]
         "\nZusätzlich zu den nebenstehenden Mitgliedsbeiträgen können Sie, wenn gewünscht, das Projekt mit einer jährlichen Spende in einer Höhe Ihrer Wahl unterstützen. Der Mitgliedschaftsantrag bietet Ihnen die Möglichkeit dazu."
         [:br {}]
         "\nAuch eine Fördermitgliedschaft ist möglich. Als Fördermitglied sind Sie allerdings nicht stimmberechtigt."
         [:br {}]
         [:br {}]
         "\nUm Mitglied zu werden, schicken Sie bitte den ausgefüllten und unterschriebenen Mitgliedschaftsantrag an die angegebene Kontaktadresse."]
        "\n                            "
        [:br {}]
        "\n                            "
        [:button {:class "button"} "Mitgliedschaftsantrag"]
        "                             \n                        "]
       "\n                    "]
      "\n                    "
      [:div
       {:class "row"}
       "\n                        "
       [:div
        {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
        "\n                            "
        [:a
         {:class
          "section-heading-bold page-heading normal-section-heading"
          :name "mitarbeit"}
         "Mitarbeit im Projekt"]
        " \n                        "]
       "\n                        "
       [:div
        {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
        "\n                            "
        [:div
         {:class "context-info"}
         "\n                                "
         [:p {:class "context-info-heading"} [:b {} "Weitere Infos"]]
         "\n                                "
         [:span {} [:u {} "eldoret-kids@t-online.de"]]
         "\n                            "]
        "\n                            "
        [:span
         {}
         "Desweiteren gibt es die Möglichkeit, Birgit Zimmermann und ihr Team vor Ort bei der Rehabilitation der Straßenkinder zu unterstützen."
         [:br {}]
         "\nFreiwillige können über einen längeren Zeitraum in Form eines Auslandspraktikums mitarbeiten."]
        "\n                            "
        [:br {}]
        " \n                        "]
       "\n                    "]
      "                     \n                "]
     "\n            "]
    "\n        "]])

(defn rundbriefarchiv [db]
  [:div
    [:div
     {:class "container"}
     [:div
      {:class "row"}
      [:div
       {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11 section"}
       [:p
        {:class
         "section-heading-bold normal-section-heading page-heading"}
        "Rundbriefarchiv"]
       (doall (for [[year rundbriefe] (db-api/get-grouped-rundbriefe db)]
                [:div
                 [:div
                  {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11 section"}
                  [:p
                   {:class
                    "section-heading-bold normal-section-heading page-heading"}
                   year]]
                 (doall (for [rundbrief (sort-by (comp - #(c/to-long (c/from-date (:rundbrief/instant %)))) rundbriefe)]
                           [:div
                            {:class
                             "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
                            [:a
                             {:href (str "rundbriefe/" (:rundbrief/file-name rundbrief))}
                             (:rundbrief/name rundbrief)]]))]))]]]])

(defn impressum [db]
  [:div
   {:class "container"}
   [:div
    {:class "row"}
    [:div
     {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 section"}
     [:div
      {:class "row"}
      [:div
       {:class "icon-heading col-xs-11 col-sm-11 col-md-11 col-lg-11"}
       [:p
        {:class "section-heading-bold page-heading"}
        "Impressum"]]
      [:div
       {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12 text-block"}
       [:span
        {}
        [:b "Angaben gemäß § 5 TMG:"][:br][:br]

        "Eldoret Kids Kenia e.V."[:br]
        "Neckartenzlinger Str. 9"[:br]
        "72658 Bempflingen"[:br]
        "Deutschland"[:br][:br]

        [:b "Vertreten durch:"][:br][:br]
        "Holger Dembek"[:br]
        "Ingrid Speth"[:br]
        "Anette Gratwohl"[:br][:br]

        "Kontakt:"[:br]
        "Telefon:"[:br]
        "07123/932353"[:br]
        "E-Mail:"[:br]
        "eldoret-kids@t-online.de"[:br][:br]

        "Registereintrag:"[:br]
        "Eintragung im Vereinsregister. Registergericht:Amtsgericht Stuttgart"[:br]
        "Registernummer: VR 1521"[:br][:br]

        "Quelle: www.e-recht24.de"[:br]

        "Haftungsausschluss (Disclaimer)
        Haftung für Inhalte
        Als Diensteanbieter sind wir gemäß § 7 Abs.1 TMG für eigene Inhalte auf diesen Seiten nach den allgemeinen Gesetzen verantwortlich. Nach §§ 8 bis 10 TMG sind wir als Diensteanbieter jedoch nicht verpflichtet, übermittelte oder gespeicherte fremde Informationen zu überwachen oder nach Umständen zu forschen, die auf eine rechtswidrige Tätigkeit hinweisen. Verpflichtungen zur Entfernung oder Sperrung der Nutzung von Informationen nach den allgemeinen Gesetzen bleiben hiervon unberührt. Eine diesbezügliche Haftung ist jedoch erst ab dem Zeitpunkt der Kenntnis einer konkreten Rechtsverletzung möglich. Bei Bekanntwerden von entsprechenden Rechtsverletzungen werden wir diese Inhalte umgehend entfernen.
        Haftung für Links
        Unser Angebot enthält Links zu externen Webseiten Dritter, auf deren Inhalte wir keinen Einfluss haben. Deshalb können wir für diese fremden Inhalte auch keine Gewähr übernehmen. Für die Inhalte der verlinkten Seiten ist stets der jeweilige Anbieter oder Betreiber der Seiten verantwortlich. Die verlinkten Seiten wurden zum Zeitpunkt der Verlinkung auf mögliche Rechtsverstöße überprüft. Rechtswidrige Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar. Eine permanente inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete Anhaltspunkte einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Links umgehend entfernen.
        Urheberrecht
        Die durch die Seitenbetreiber erstellten Inhalte und Werke auf diesen Seiten unterliegen dem deutschen Urheberrecht. Die Vervielfältigung, Bearbeitung, Verbreitung und jede Art der Verwertung außerhalb der Grenzen des Urheberrechtes bedürfen der schriftlichen Zustimmung des jeweiligen Autors bzw. Erstellers. Downloads und Kopien dieser Seite sind nur für den privaten, nicht kommerziellen Gebrauch gestattet. Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt wurden, werden die Urheberrechte Dritter beachtet. Insbesondere werden Inhalte Dritter als solche gekennzeichnet. Sollten Sie trotzdem auf eine Urheberrechtsverletzung aufmerksam werden, bitten wir um einen entsprechenden Hinweis. Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Inhalte umgehend entfernen.Quellenangaben: Disclaimer von eRecht24, dem Portal zum Internetrecht von Rechtsanwalt Sören Siebert,Disclaimer von eRecht24, dem Portal zum Internetrecht von Rechtsanwalt Sören Siebert"]]]]]])


(defn footer [db]
  [:footer
   {:class "footer"}
   [:div
    {:class "container"}
    [:div
     {:class "row"}
     [:a
      {:class
       "section col-sm-4 col-xs-4 col-md-3 col-lg-3 footerelement"
       :href "/resources/mitgliedschaftsantrag.pdf"}
      [:span {:class "footerlink"} "Mitgliedschaftsantrag"]]
     [:a
      {:class
       "section col-sm-4 col-xs-4 col-md-3 col-lg-3 footerelement"
       :href "/resources/patenschaftsantrag.pdf"}
      [:span {:class "footerlink"} "Patenschaftsantrag"]]
     [:a
      {:class
       "section col-sm-4 col-xs-4 col-md-3 col-lg-3 footerelement"
       :href "/resources/satzung.pdf"}
      [:span {:class "footerlink"} "Satzung"]]
     [:a
      {:class
       "section col-sm-4 col-xs-4 col-md-3 col-lg-3 footerelement"
       :href (str "resources/rundbriefe/" (db-api/get-current-rundbrief-name db))}
      [:span {:class "footerlink"} "Rundbrief"]]]
    [:div
     {:class "row"}
     [:div
      {:class
       "section col-md-6 col-sm-6 col-xs-6 col-lg-6 footerelement"}
      "© 2017 Eldoret Kids Kenia e.V."]
     [:a
      {:class
       "col-xs-6 col-sm-6 col-md-6 col-lg-6 section"
       :href "/impressum"}
      [:span {:class "footerlink"} "Impressum"]]]]])
