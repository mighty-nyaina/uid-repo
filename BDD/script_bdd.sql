-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Client :  localhost:8889
-- Généré le :  Dim 17 Avril 2016 à 23:57
-- Version du serveur :  5.5.42
-- Version de PHP :  5.6.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Base de données :  `uidmadapp`
--

-- --------------------------------------------------------

--
-- Structure de la table `ALERT_NOTIFICATION`
--

CREATE TABLE `ALERT_NOTIFICATION` (
  `ALERT_ID` int(11) NOT NULL,
  `ALERT_USER_SENDER` int(11) NOT NULL,
  `ALERT_USERID` int(11) NOT NULL,
  `ALERT_TAGUIID` int(11) NOT NULL,
  `ALERT_LONGITUDE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `ALERT_LATITUDE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `ALERT_DATE` date NOT NULL,
  `ALERT_HOPITALSIGNALER` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `HOPITAL`
--

CREATE TABLE `HOPITAL` (
  `ID_HOPITAL` int(11) NOT NULL,
  `NOM` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LONGITUDE` decimal(10,6) DEFAULT NULL,
  `LATITUDE` decimal(10,6) DEFAULT NULL,
  `EMAIL` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `HOPITAL`
--

INSERT INTO `HOPITAL` (`ID_HOPITAL`, `NOM`, `LONGITUDE`, `LATITUDE`, `EMAIL`) VALUES
(1, 'HJRA', '47.517970', '-18.919805', 'hjra@yopmail.com'),
(2, 'Hôpital Général de Befelatanana', '47.523195', '-18.920495', 'hbefelatanana@yopmail.com'),
(3, 'Polyclinic Ilafy', '47.549948', '-18.854972', 'polyilafy@yopmail.com'),
(4, 'HOMI', '47.542727', '-18.898181', 'homi@yopmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `MALADIE`
--

CREATE TABLE `MALADIE` (
  `ID_MALADIE` int(11) NOT NULL,
  `TITRE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `IMG` text COLLATE utf8_unicode_ci,
  `DESCRIPTION` text COLLATE utf8_unicode_ci,
  `CONSEIL_DESCRIPTION` text COLLATE utf8_unicode_ci,
  `CONSEIL_VIDEO` text COLLATE utf8_unicode_ci,
  `CONSEIL_IMG` text COLLATE utf8_unicode_ci
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `MALADIE`
--

INSERT INTO `MALADIE` (`ID_MALADIE`, `TITRE`, `IMG`, `DESCRIPTION`, `CONSEIL_DESCRIPTION`, `CONSEIL_VIDEO`, `CONSEIL_IMG`) VALUES
(1, 'Epilepsie', 'epi2.jpg', 'Epilepsie', '<p><b>1.</b> Demeurer calme et laisser la crise suivre son cours.</p>\n<p><b>2.</b> Noter la durée de la crise.</p>\n<p><b>3.</b> Éviter les blessures. Si nécessaire, aider la personne à s’étendre sur le sol, éloigner les objets durs ou pointus et placer un objet mou sous sa tête.</p>\n<p><b>4.</b> NE PAS immobiliser la personne.</p>\n<p><b>5.</b> NE RIEN METTRE DANS SA BOUCHE.</p>\n<p><b>6.</b> Tourner la personne sur le côté afin de garder les voies respiratoires libres.</p>\n<p><b>7.</b> Après la crise, il faut réconforter la personne et la rassurer sur ce qui vient de se passer. Il faut demeurer avec la personne jusqu’à ce qu’elle soit pleinement consciente de son environnement. Il est possible que la personne ait besoin de se reposer ou de dormir.</p>', NULL, NULL),
(2, 'Hypoglycémie', 'epi2.jpg', 'Hypoglycémie', '<p> Si la victime est consciente : donnez-lui rapidement du sucre ou des produits sucrés (morceaux de sucre, confiture, banane, pain) afin d''augmenter sa glycémie.</p> <p> Si la personne est inconsciente :</p> <p><b>1.</b> Prévenez immédiatement les secours et informez-les que la victime est diabétique. libérez ses voies aériennes et vérifiez ses constantes vitales.</p> <p><b>2. </b>Si vous y avez été formé, placez la victime en position latérale de sécurité.</p> <p><b>3.</b> Si la personne ne respire pas, procédez en urgence aux techniques de réanimation si vous y êtes formé.</p>', NULL, NULL),
(3, 'Problème cardiaque', 'epi2.jpg', 'Problème cardiaque', '<p><b>2.</b> Masser : il faut débuter immédiatement le massage cardiaque en attendant la mise en place d''un défibrillateur. Le massage cardiaque consiste à appuyer régulièrement et fermement sur le thorax d''une victime. Ces compressions thoraciques font circuler le sang dans le corps lorsque le cœur ne peut plus le faire lui-même.\n- Victime allongée sur le dos, par terre, on se place à genoux auprès de la victime.\n- On place le talon d''une main au centre de la poitrine, strictement sur la ligne médiane, jamais sur les côtés et l''autre main au-dessus de la première et on appuie de 4 à 5 cm bras tendus, coudes non fléchis.\n- On relâche immédiatement la pression pour que la paroi remonte (décompression). La poitrine doit reprendre sa dimension initiale après chaque compression.\n- On réalise le geste 100 fois par minute selon le rythme de la chanson <<Staying alive>> des Bee Gees.\n</p>', NULL, NULL),
(4, 'Alzheimer', 'epi2.jpg', 'Alzheimer', '<p><b>1.</b> Rassurer la personne</p>\n<p><b>2.</b> Lui demander gentillement s''il a une pièce d''identité ou le numero d''un proche</p>\n<p><b>3.</b> L''escorter vers une commissariat ou attendre les secours (Secours UID Madapp)</p>\n<p><b>4.</b> Attendre avec lui afin de lui rassurer</p>\n<p><b>5.</b> Améliorer la communication en attendant de l''aide</p>', NULL, NULL),
(5, 'Asthme', 'epi2.jpg', 'Asthme', '<p>Mettez la victime en position demi-assise * dans une pièce aérée (* : C''est une position dans laquelle la victime est assise et adossée par exemple à un mur. Cette position permet à la victime de se décontracter tout en libérant ses voies aériennes).</p>\n<p>Desserrez ses vêtements pour faciliter sa respiration, puis rassurez la.</p>\n<p>Si la victime a ses médicaments sur elle, veillez à ce qu''elle les prenne bien (mettez les lui éventuellement dans la main).</p>\n<p>Si la victime a des difficultés à parler, si ses lèvres deviennent bleues ou si son pouls s''accélère Alertez ou faites alerter les secours ou son médecin traitant.</p>', NULL, NULL),
(6, 'Diabète', 'epi2.jpg', 'Diabète', '<h3>L''hyperglycémie</h3>\n<p>Il s''agit d''un taux élevé de sucre dans le sang, qui correspond souvent à l''épisode de découverte du diabète, mais qui peut survenir aussi en cas d''excès de table, chez un diabétique traité, ou à la suite du mauvais suivi du traitement.</p>\n<p>Le patient dit avoir un diabète;\nIl présente une respiration ample et difficile ;\nSon pouls est en général rapide ;\nSon haleine à une discrète odeur d''acétone (odeur proche de celle du vernis à ongles) ;\nIl a beaucoup soif et urine fréquemment.</p>\n\n\n<h4>Conduite à tenir</h4> ?\n<p>Après avoir recueilli l''ensemble des signes, appelez le médecin traitant et les secours ;\nEn cas de troubles de la conscience, mettez la victime en position latérale de sécurité et surveiller sa respiration ;\nDans tous les cas conformez-vous aux indications transmises par le médecin.</p>\n\n\n<h3>L''hypoglycémie</h3>\n\n<p>Il s''agit d''une diminution du taux de sucre dans le sang qui peut survenir chez un diabétique traité qui s''est trompé dans ses doses d''insuline ou bien qui n''a pas assez mangé.\nLa victime ressent une sensation de faiblesse, transpire, est pâle. Parfois elle présente des troubles du comportement (désorientation, agressivité ), qui peuvent faire croire à un état d''agitation psychiatrique.</p>\n\n<h4Conduite à tenir ?</h4>\n<p>\nLe plus souvent, les diabétiques sont préparés à de tels incidents, et corrigent par eux-mêmes en absorbant du sucre.\nSi la victime ne peut subvenir à ses besoins : </p>\n\n<p>\nAidez-la à s''asseoir ou à s''allonger ;\nAdministrer lui une boisson sucrée.\nSi le malaise cesse, laissez-la se reposer et conseillez-lui de consulter son médecin. Appeler les secours </p>', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `MALADIE_PATIENT`
--

CREATE TABLE `MALADIE_PATIENT` (
  `PATIENT_ID_PATIENT` int(11) NOT NULL,
  `MALADIE_ID_MALADIE` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `MALADIE_PATIENT`
--

INSERT INTO `MALADIE_PATIENT` (`PATIENT_ID_PATIENT`, `MALADIE_ID_MALADIE`) VALUES
(1, 1),
(1, 2),
(9, 1);

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `maladie_user`
--
CREATE TABLE `maladie_user` (
`ID_MALADIE` int(11)
,`TITRE` varchar(100)
,`IMG` text
,`DESCRIPTION` text
,`CONSEIL_DESCRIPTION` text
,`CONSEIL_VIDEO` text
,`CONSEIL_IMG` text
,`IDUSER` int(11)
);

-- --------------------------------------------------------

--
-- Structure de la table `PATIENT`
--

CREATE TABLE `PATIENT` (
  `ID_PATIENT` int(11) NOT NULL,
  `NOM` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PRENOM` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SEXE` int(11) NOT NULL DEFAULT '0',
  `DATE_NAISSANCE` date DEFAULT NULL,
  `ADRESSE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONTACT` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EMAIL` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONTACT_URGENCE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FB_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHOTO` text COLLATE utf8_unicode_ci
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `PATIENT`
--

INSERT INTO `PATIENT` (`ID_PATIENT`, `NOM`, `PRENOM`, `SEXE`, `DATE_NAISSANCE`, `ADRESSE`, `CONTACT`, `EMAIL`, `CONTACT_URGENCE`, `PASSWORD`, `FB_ID`, `PHOTO`) VALUES
(1, 'JEAN', 'VALJEAN', 0, '1959-11-12', NULL, NULL, 'mail@gmail.com', NULL, 'admin', NULL, NULL),
(9, 'est', 'bdbs', 0, '1992-10-10', 'a', '', 'test@gmail.com', NULL, 'a', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `TAG`
--

CREATE TABLE `TAG` (
  `ID_TAG` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `PATIENT_ID_PATIENT` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `TAG`
--

INSERT INTO `TAG` (`ID_TAG`, `PATIENT_ID_PATIENT`) VALUES
('04C7107A7A4884', 1);

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `view_patient_maladie`
--
CREATE TABLE `view_patient_maladie` (
`PATIENT_ID_PATIENT` int(11)
,`ID_MALADIE` int(11)
,`TITRE` varchar(100)
,`IMG` text
,`DESCRIPTION` text
,`CONSEIL_DESCRIPTION` text
,`CONSEIL_VIDEO` text
,`CONSEIL_IMG` text
);

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `view_tag_patient`
--
CREATE TABLE `view_tag_patient` (
`ID_TAG` varchar(50)
,`ID_PATIENT` int(11)
,`NOM` varchar(50)
,`PRENOM` varchar(50)
,`SEXE` int(11)
,`DATE_NAISSANCE` date
,`ADRESSE` varchar(50)
,`CONTACT` varchar(50)
,`EMAIL` varchar(50)
,`CONTACT_URGENCE` varchar(50)
,`PASSWORD` varchar(50)
,`FB_ID` varchar(50)
,`PHOTO` text
);

-- --------------------------------------------------------

--
-- Structure de la vue `maladie_user`
--
DROP TABLE IF EXISTS `maladie_user`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `maladie_user` AS select `m`.`ID_MALADIE` AS `ID_MALADIE`,`m`.`TITRE` AS `TITRE`,`m`.`IMG` AS `IMG`,`m`.`DESCRIPTION` AS `DESCRIPTION`,`m`.`CONSEIL_DESCRIPTION` AS `CONSEIL_DESCRIPTION`,`m`.`CONSEIL_VIDEO` AS `CONSEIL_VIDEO`,`m`.`CONSEIL_IMG` AS `CONSEIL_IMG`,`mp`.`PATIENT_ID_PATIENT` AS `IDUSER` from (`maladie` `m` join `maladie_patient` `mp`) where (`m`.`ID_MALADIE` = `mp`.`MALADIE_ID_MALADIE`);

-- --------------------------------------------------------

--
-- Structure de la vue `view_patient_maladie`
--
DROP TABLE IF EXISTS `view_patient_maladie`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_patient_maladie` AS select `mp`.`PATIENT_ID_PATIENT` AS `PATIENT_ID_PATIENT`,`m`.`ID_MALADIE` AS `ID_MALADIE`,`m`.`TITRE` AS `TITRE`,`m`.`IMG` AS `IMG`,`m`.`DESCRIPTION` AS `DESCRIPTION`,`m`.`CONSEIL_DESCRIPTION` AS `CONSEIL_DESCRIPTION`,`m`.`CONSEIL_VIDEO` AS `CONSEIL_VIDEO`,`m`.`CONSEIL_IMG` AS `CONSEIL_IMG` from (`maladie` `m` join `maladie_patient` `mp`) where (`mp`.`MALADIE_ID_MALADIE` = `m`.`ID_MALADIE`);

-- --------------------------------------------------------

--
-- Structure de la vue `view_tag_patient`
--
DROP TABLE IF EXISTS `view_tag_patient`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_tag_patient` AS select `t`.`ID_TAG` AS `ID_TAG`,`p`.`ID_PATIENT` AS `ID_PATIENT`,`p`.`NOM` AS `NOM`,`p`.`PRENOM` AS `PRENOM`,`p`.`SEXE` AS `SEXE`,`p`.`DATE_NAISSANCE` AS `DATE_NAISSANCE`,`p`.`ADRESSE` AS `ADRESSE`,`p`.`CONTACT` AS `CONTACT`,`p`.`EMAIL` AS `EMAIL`,`p`.`CONTACT_URGENCE` AS `CONTACT_URGENCE`,`p`.`PASSWORD` AS `PASSWORD`,`p`.`FB_ID` AS `FB_ID`,`p`.`PHOTO` AS `PHOTO` from (`tag` `t` join `patient` `p`) where (`t`.`PATIENT_ID_PATIENT` = `p`.`ID_PATIENT`);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `ALERT_NOTIFICATION`
--
ALTER TABLE `ALERT_NOTIFICATION`
  ADD PRIMARY KEY (`ALERT_ID`),
  ADD KEY `fk_alert_useridsender` (`ALERT_USER_SENDER`),
  ADD KEY `fk_alert_useridowner` (`ALERT_USERID`),
  ADD KEY `fk_alert_hopital` (`ALERT_HOPITALSIGNALER`);

--
-- Index pour la table `HOPITAL`
--
ALTER TABLE `HOPITAL`
  ADD PRIMARY KEY (`ID_HOPITAL`);

--
-- Index pour la table `MALADIE`
--
ALTER TABLE `MALADIE`
  ADD PRIMARY KEY (`ID_MALADIE`);

--
-- Index pour la table `MALADIE_PATIENT`
--
ALTER TABLE `MALADIE_PATIENT`
  ADD PRIMARY KEY (`PATIENT_ID_PATIENT`,`MALADIE_ID_MALADIE`),
  ADD KEY `FK_MALADIE_PATIENT2` (`MALADIE_ID_MALADIE`);

--
-- Index pour la table `PATIENT`
--
ALTER TABLE `PATIENT`
  ADD PRIMARY KEY (`ID_PATIENT`);

--
-- Index pour la table `TAG`
--
ALTER TABLE `TAG`
  ADD PRIMARY KEY (`ID_TAG`),
  ADD KEY `FK_ASSOCIATION_3` (`PATIENT_ID_PATIENT`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `ALERT_NOTIFICATION`
--
ALTER TABLE `ALERT_NOTIFICATION`
  MODIFY `ALERT_ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `HOPITAL`
--
ALTER TABLE `HOPITAL`
  MODIFY `ID_HOPITAL` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT pour la table `MALADIE`
--
ALTER TABLE `MALADIE`
  MODIFY `ID_MALADIE` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `PATIENT`
--
ALTER TABLE `PATIENT`
  MODIFY `ID_PATIENT` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;