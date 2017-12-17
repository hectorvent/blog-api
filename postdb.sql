-- MySQL dump 10.16  Distrib 10.2.9-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: postdb
-- ------------------------------------------------------
-- Server version	10.2.9-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `postId` int(11) NOT NULL,
  `email` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `body` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_1_idx` (`postId`),
  CONSTRAINT `fk_comment_1` FOREIGN KEY (`postId`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,1,'juanpalotes@gmail.com','Juan de los palotes','Esta haciendo mucho frio en el aula de clase javascript'),(3,1,'otrojuan@correo.com','Otro Apellido Juan','Deberia apagar el aire'),(4,6,'juanp@correo.com','Juan Perez','Buena Publicacion'),(5,7,'juanp@correo.com','Juan Perez','Java'),(6,9,'juanp@correo.com','Juan Perez','Buena Publicacion!!'),(7,8,'juanp@correo.com','Juan Perez','suchhh. Quieren Controlar el mundo'),(8,5,'juanp@correo.com','Juan Perez','Saludos !!!!'),(9,10,'juanp@correo.com','Juan Perez','Good Job'),(10,12,'juanp@correo.com','Juan Perez','Oooooooooo'),(11,13,'juanp@correo.com','Juan Perez','Good Job'),(12,15,'juanp@correo.com','Juan Perez','Interesante'),(17,1,'juame@hhhh','Juan Mendez','prueba'),(19,1,'peperez@dominio.com','pedro perez','otra prueba'),(20,1,'paomejia@gmail.com','Paola Mejia','probando la redireccion'),(22,2,'correo@gmail.com','prueba','comentario'),(24,1,'yinet.jaquez@hotmail.com','yinet jaquez de la cruz','cuerpo\n				'),(25,17,'juanp@coreo.com','Juan Perez','Es cierto !!!!!'),(26,22,'josepeña@gmail.com','Jose Peña','son chinas, pero son buenas'),(27,5,'hu','Darvis','hu');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `body` text NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_post_1_idx` (`userId`),
  CONSTRAINT `fk_post_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'Clase JavaScript Domingo2','Esta clase ha sido un exito',1),(2,'Pruebas de Envio','Sigue haciendo frio',12),(3,'Post De Prueba','Post para probar el servicio',18),(4,'holav','rcevc',13),(5,'Prueba Darvis','Saludos a todos.',14),(6,'Frida Kahlo',' fue una pintora mexicana. Su vida estuvo marcada por el infortunio de contraer poliomielitis y después por un grave accidente en su juventud que la mantuvo postrada en cama durante largos periodos, llegando a someterse hasta a 32 operaciones quirúrgicas. Llevó una vida poco convencional.\n				',13),(7,'Javascript','JavaScript (abreviado comúnmente JS) es un lenguaje de programación interpretado, dialecto del estándar ECMAScript. Se define como orientado a objetos, basado en prototipos, imperativo, débilmente tipado y dinámico.',18),(8,'GOOGLE',' Es una compañía principal subsidiaria de la multinacional estadounidense Alphabet Inc., cuya especialización son los productos y servicios relacionados con Internet, software, dispositivos electrónicos y otras tecnologías. \n				',13),(9,'POSTEAR','Postear es la acción de enviar un mensaje a un grupo de noticias, foro de discusión, comentarios en sitio web o un blog, a una publicación de Facebook o en Twitter, etc. \n				',13),(10,'CSS','es el lenguaje utilizado para describir la presentación de documentos HTML o XML, esto incluye varios lenguajes basados en XML como son XHTML o SVG. CSS describe como debe ser renderizado el elemento estructurado en pantalla, en papel, hablado o en otros medios.		',13),(11,'Limpiar campo','Como podría hacer en JavaScript de el campo que tenga el foco al pulsar un botón de \"Limpiar\". Ya aparte, tengo el botón RESET que los limpia todos los campos.\n				',13),(12,'TIRED',' i\'m tired\n				',13),(13,'Prueba','Esto es una prueba',15),(14,'INGLES','El idioma inglés es una lengua germánica occidental que surgió en los reinos anglosajones de Inglaterra y se extendió hasta el Norte en lo que se convertiría en el sudeste de Escocia, bajo la influencia del Reino de Northumbria.',13),(15,'Estados Unidos levanta restricciones a exportaciones dominicanas por mosca del Mediterráneo','SANTO DOMINGO. El Ministerio de Agricultura informó que recibió la Orden Federal DA-2017-38 de las autoridades sanitarias de los Estados Unidos, donde permiten la entrada oficial de frutas y vegetales de las siete provincias que se mantenían vedadas en la República Dominicana.\n\nLa misiva, fechada 12 de diciembre del año 2017, levanta las restricciones para las provincias de la zona este que todavía se mantenían sin poder exportar al principal socio internacional. La medida abre completamente los servicios de exportación de frutas y vegetales de dicha región con lo que se completa todo este mercado dominicano para entrada a los Estados Unidos, dijo el Ministerio en una nota de prensa.\n\nCon la disposición reconocen la declaratoria de las autoridades dominicanas y del Ministerio de Agricultura, de que el país está libre de la mosca del Mediterráneo; cuyo foco fue detectado en la localidad de Punta Cana, provincia La Altagracia, y reportado en fecha 18 de marzo del año 2015.',16),(16,'prueba de post','asd',7),(17,'Hola a todos','La vida es bella',8),(18,'Diplomado JavaScript + CSS','ITLA Santiago \n					',24),(19,'INFOXICACION (Intoxicación por sobrecarga de Información)',' Hoy día, nos encontramos cada vez más con áreas del saber donde el individuo debe tomar la decisión de especializarse en ciertas competencias dentro de su radio de acción. Por eso vemos que existe un profesional de la medicina especializado en:\n\n\nManos.\nPies.\nCerebro.\nRodillas.\nCorazón.\n...........\nEn fin, parece que en cada profesión las exigencias del mercado marcan la tendencia de la especialización. Aun así, toda persona sin importar su formación, nivel de educación esta siendo bombardeado de manera constante y sistemática de una cantidad considerable de información por diversos canales tales como:\nRadio.\nTelevisión.\nRevistas.\nPeriódicos.\nInternet.\nEsta avalancha de data, se corrobora en la frase de Simon (1971) \"una riqueza de información crea una pobreza de atención\". El termino Infoxicación se utiliza para referirse a una sobrecarga de información difícil de procesar. Surge de la unión de las palabras información e intoxicación.\n\nEl neologismo Infoxicación, citado por Alfons Cornella en 1996, hace referencia al concepto de que la sobrecarga de información que recibe un usuario, en especial de Internet en todas sus formas, puede causarle la sensación de no poder abarcarla ni gestionarla y, por tanto, llegar a generarle una gran angustia.\n\nEl uso, cada vez mayor, de dispositivos inteligentes (celulares, tabletas) viene a definir un entorno fértil para el incremento de la Infoxicación; si el usuario no posee un manejo adecuado de inteligencia emocional mas disciplina y enfoque en los objetivos trazados, su rendimiento se vera afectado de manera considerable por la sobrecarga y saturación de información que recibe. Con el avance abrumador de la tecnología (combinando sus armas poderosas: hardware y software), se vuelve común el que las personas quieran dominar cualquier herramienta de la noche a la mañana, traduciéndose en un sentimiento de angustia y sensación de impotencia. \n\nEl enfoque para poder estar un paso por delante de la intoxicación por sobrecarga de información seria:\nDefinir que información es critica para mis intereses.\nEstablecer prioridades.\nSelección adecuada de motores de búsqueda.\nIdentificar filtros personalizados.\nResolución de casos individuales. \n\nEn conclusión, soy de los que pienso que la generación de información continuara de manera sostenida y exponencial; por lo que debemos acostumbrarnos a vivir con la infoxicación y tratar de ser mas específicos en lo que hacemos e ir cerrando el cerco de las competencias a desarrollar en la especialización de que somos objeto.\n\nInformación adicional en:\nComo sobrevivir a la infoxicación. Trascripción de la conferencia del acto de entrega de títulos de los programas de Formación de Posgrado del año académico 1999-2000. \n\n					',24),(20,'La realidad de la vida','La vida y la realidad son dos cosas distintas.\n\nLa primera, la vida, es la obvia consciencia de tu existencia. Es el movimiento constante del fluir de La Existencia. Manifestándose. Expresándose. Mostrándose. Apareciendo y desapareciendo.\n\nTodo en el espacio de la consciencia espiritual que eres.\n\nLa realidad, es la experiencia que tienes de la vida. Es la manera particular en la que vives tu existir. Es la mirada de la vida desde una perspectiva particular del infinito.\n\nCuriosamente, hemos aprendido a creer que el juego de nuestro existir tiene que ver con «la vida». Con lo que aparece: circunstancias, personajes, pensamientos, emociones. Y con lo que desaparece y se desvanece.\n\nHemos creído que nuestra existencia, y muy especialmente, la calidad de nuestro existir (si somos felices o miserables), depende de «la vida» y la forma en que se muestra.\n\nAprendimos a luchar. A proteger. A intentar controlar. A pretender predecir.\n\nY en el proceso, a creernos al efecto de la vida.\n\nHemos dado por sentado que nuestra felicidad, nuestra plenitud, nuestra libertad, nuestro entusiasmo, son experiencias condicionadas a las circunstancias.\n\nPero resulta que esos estados de ser, no tienen que ver con la vida. Sino con tu realidad. Esa que creas desde las miradas y las creencias con las que te encuentras con la obviedad de la vida en movimiento.',27),(21,'dgs','sdg',9),(22,'Lenovo','lenovo es una marca de china',18),(23,'vida diaria ','Se entiende como vida a la existencia. El término suele aludir a la actividad que lleva a cabo un ser orgánico o, con mayor precisión, a su capacidad de nacer, desarrollarse, reproducirse y morir. Lo cotidiano, por otra parte, es aquello que se realiza todos los días.',27);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `userId` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  KEY `fk_token_1_idx` (`userId`),
  CONSTRAINT `fk_token_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,'bf4da90b-3b60-4d29-aab6-d1c2ca285063'),(1,'d3debcbb-15f9-4c27-9beb-fb2c07a9c96a'),(10,'78ff2a2f-e550-447c-8269-44f1b2f876c1'),(7,'6f8abe28-a36b-4854-91d0-2a5fd8c34dfc'),(8,'7dd1a511-9b6a-4d0f-bd0f-a066b5e623bd'),(7,'b0c4352b-75f5-4e0a-86b7-388b6406a531'),(7,'77606130-16fc-45f7-96a5-d750755dcf59'),(10,'43854637-1109-44a0-9b09-d5011c06a6fb'),(11,'f17da5a3-5656-4da5-9dc2-0ee942f2eab6'),(10,'8017bc07-4f08-4eca-b591-43c9a61f49ad'),(12,'f91c555a-8107-40d7-8b30-0c6ead4f8224'),(7,'042ae2be-e92e-4232-b130-b16fa932bd68'),(7,'b02a0e9f-dc32-4bc7-8b63-23f457c63565'),(14,'2c0caf0c-a4c2-40cf-b3e9-c4fe46f9c5d3'),(9,'6af8e889-52ce-4448-b90f-67b8fba9e381'),(15,'f6b4d4f4-4311-41a0-b5fb-3ba575b0439c'),(7,'24f9c3f8-4a9d-4c08-9ffb-7de3c293327f'),(11,'1e999402-fb7d-4e7b-8f4a-0d0b235e3297'),(11,'50c4b5e7-61ac-4e0a-a99c-13a26c53a002'),(14,'0ed601d3-248f-4af2-ba87-751bf7ca5f76'),(13,'be4327ea-8ea6-40cf-bdc7-fae5f01759d3'),(13,'73441ec7-da7e-4577-b982-d9d746763376'),(7,'db943b00-4950-4489-b3aa-cfc2b4e8eeab'),(9,'379ea414-c400-4fd5-9532-358fb6258c4f'),(12,'8a97f3a4-c653-4706-8603-3793ab1ee5b2'),(12,'9609378d-ba4a-4675-b1b0-57f00020667e'),(11,'3f4d52b6-1b48-4a5d-a7cf-0a2a98c3e2ae'),(12,'d8588609-d884-4b41-86f3-33463db564ec'),(17,'c2773633-62a2-499a-b17c-90743a126102'),(8,'0bffa363-77dd-4a89-95e7-22553e3cad0d'),(7,'b70886e6-58c6-4ba6-ba19-1ff27cd4f6cb'),(9,'b0cf5fa1-7d13-4801-866e-cdb5b8764c5f'),(18,'189d3691-7323-48be-897c-23e31d292725'),(9,'b43381fc-d3f6-4af0-be9c-a0a7bf56e4cc'),(13,'1cf6e027-b7d0-4351-91a6-d5dcc97ebb9f'),(13,'c8b35ac5-9b66-4a3d-b265-be073866c0cf'),(7,'e6ed93bd-1353-4ac5-b441-cc1c2fd2f218'),(7,'b3644974-c6a4-4b56-a83a-91a9ef16b87c'),(18,'bf717cc5-0c32-4131-a09f-de42b2a0fa4d'),(18,'eb1465d0-7c4d-44c3-9d47-360a902f1ebb'),(18,'4f2fac31-1807-43aa-8e69-25d9c01314dd'),(10,'d572b5f6-abd2-484d-a5aa-75ab5f72bd64'),(13,'ddc6ca12-83f3-49e7-9426-2050eb10d1e5'),(13,'7d92668e-e246-4c12-8d7d-2466ece2660e'),(7,'8615ebb6-2aa5-494b-8374-5b17c6612054'),(9,'2e0b5fe9-e79b-4a9f-82c1-1983b6b58275'),(18,'d6b70b23-c45f-4e69-8d81-1632d2654937'),(7,'d3837f4b-e3a8-4a53-af89-a256a1bbe42e'),(15,'981f59e6-c007-4421-9058-0d8390748846'),(16,'7d524df3-fa3d-4b65-9eb0-5b4a1894e6fa'),(9,'748df036-dbeb-4dda-9577-3692fb2037ab'),(15,'16f5e17c-5a49-4dfc-8481-9c683c7727a6'),(7,'ceb3c97b-7143-4f2b-a287-7e7916da4c90'),(19,'7b912e73-35a1-4b50-8193-83e1c600634c'),(8,'2c451a12-ede6-4c1e-bc86-02dd74235bd3'),(12,'c1835355-028f-42c7-80b1-069847c1dc14'),(8,'71283a15-5ba5-47cd-a176-16787639eef3'),(14,'67e652fa-c173-4d69-a1de-3f9d981b5a77'),(13,'f41cd891-af61-4863-b366-a38bcc724957'),(14,'1717c6e3-bcdc-4109-b163-ff1c848932be'),(12,'4c209393-b09e-45a1-b175-1f382e811a95'),(7,'9572a620-d7eb-432c-915c-9ba88bc552ee'),(9,'23226088-6895-472b-b375-5dbe6f8245a2'),(7,'bcc72845-0fd1-4183-a868-082faaca776f'),(14,'69b92ff3-7431-412f-9aab-8537a3ad46b1'),(7,'ba8cf1f4-a8d7-4ef4-93bf-0859e4ff94d4'),(16,'b5a72589-410f-45e1-893f-88e32ad6dff1'),(7,'bd063c9d-53e9-4a2a-8b43-6dc1c305ee31'),(7,'9345b78a-a952-425d-91a9-729e61f1c05c'),(16,'3f1f3253-0ff4-4536-9c4b-eb78de7dba12'),(7,'6ea3b4f2-2913-4a24-bc0c-f55c01dae391'),(7,'76197317-482f-4b19-b7b6-34f968a55f2f'),(7,'573566e9-a72a-4a8c-bfb3-d4ef0f64c801'),(16,'612ad41f-4edc-42aa-b4a4-3cbedea6701b'),(16,'1fbc4251-241d-495e-a29e-40a2ffb8fa3c'),(15,'94c0ffae-6b97-4a0a-a6cc-8cbf7a06bc36'),(13,'b81b2cf5-95a4-42bc-95e9-b906f3f3f364'),(15,'bb09cc4e-2d71-439a-b649-6089eaaf29b1'),(13,'0205fbf1-b2d6-4bc1-8aa5-677d19f69e7d'),(15,'16e2b810-302b-429a-8ce3-3272b609f526'),(7,'8cc2c9ad-415e-40fc-b5ce-fe0f937d5975'),(8,'696299bf-27e7-4ff0-8968-2ab235ab65ce'),(8,'738d6676-d5d7-4847-878a-05a8ea2b461b'),(8,'0ecf372d-3be4-4b96-ab99-7dde528d8d98'),(16,'0a9ec5a2-6494-448a-8b71-8bdaf4e49346'),(15,'e6abce12-8e6f-4f91-9af0-3bc06ca6fba4'),(7,'61a3aec2-5990-49d5-a15b-c8fef4ea5c7c'),(7,'01239c88-d9ff-4eca-abf5-71adb91989ad'),(13,'c8d59570-e2f5-49f3-bab2-3cbd0a0db06e'),(7,'a8b5963e-f893-4995-87ad-5109a0c09b47'),(24,'46256123-aff0-47a8-918e-89009c150cde'),(13,'045de163-55cc-4b6d-83bd-dd399fae1ff0'),(15,'60a0cb84-76db-4486-b05f-84421fc27d35'),(8,'1ada8cdd-91e6-408c-ae99-77e7394314d8'),(24,'511bb161-60dd-4ea4-9368-91ef3a87da36'),(15,'bd3b458c-5f1c-4611-b948-5bf32f90bcaa'),(24,'14bcf659-66a5-43c1-b8bd-24fd89c265c7'),(24,'f3af737e-3300-4e76-ad6d-4ab01783d224'),(16,'47836f7f-8339-4a45-8d10-0e06ea3729ec'),(16,'eace7f30-73ad-409b-b9e5-a0505d3ff9ab'),(7,'1a1f69a1-29cc-4edc-9e7e-5f208ec48e25'),(16,'4776ee06-a544-4220-805c-0a088fc957b6'),(15,'375e7787-b1d0-48c9-b8bf-fd9e3b59f548'),(16,'fe53201b-e28d-4518-8276-d012615c9092'),(13,'0cfd98bb-5450-4eea-8ac1-da27e4a2f36e'),(16,'e1e0af36-36f6-4f02-a198-ae8127f17a2f'),(7,'fcb29424-c254-4df7-a65f-785d3cf1c8e3'),(24,'fb56cc20-82ea-4af9-851b-14a23d26c5c3'),(24,'e10892f9-5b02-419e-becc-0117fa2670fc'),(18,'a985da3f-c524-4ef4-824d-99eaebc825a5'),(24,'dda5fa29-4f21-4c6b-a640-d5a17c6e706a'),(24,'90ac61a5-0da5-4ec1-89d1-4131d9c5ba26'),(12,'2cc1cc64-5222-4f6f-a5e6-4bccad7bb4e3'),(20,'979c3dbd-4f91-40f4-8385-e2490f87a965'),(28,'7fd47105-fade-4c7f-95ab-4514bbc8966b'),(12,'5005eac7-6dfa-4e9a-bb04-ddbbd7ed22db'),(18,'c0ec4e3c-46c2-46e2-837e-90b4968d5b0f'),(12,'8e18b4db-342f-4035-b7e6-f55305d9aa4e'),(27,'7e34ecce-514b-4f3f-abac-08779dfce3d6'),(27,'eb60239b-1228-4169-8980-a60ec25eea97'),(12,'a91b7ec6-01c7-48e6-a789-866a7dfca153'),(27,'e0c95231-9ea9-48aa-b174-efbd6ba9fa6f'),(12,'6035d698-9e72-4e33-9062-3653c0b08714'),(18,'8d50f7f3-dc10-4349-a61d-b29265c72873'),(27,'9ae0f2c3-665d-4a46-860c-8273669f998f'),(27,'e58d62cc-f759-4e0f-9162-e84cc9aa3797'),(27,'bc5ab414-1fc3-40f6-9ce0-53170caa9a14'),(18,'d7e97fad-2ff8-4a7f-8e09-ad297f7bcc75'),(16,'f67dc486-75c4-43c8-ab10-b4988a422664');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Hector Ventura','hectorvent@gmail.com','12345'),(2,'Hector Ventura Reyes','hventura@gmail.com','123452'),(7,'Juan Carlos','juan@juan','asd'),(8,'Escarlin Moran','escarlin@hotmail.com','123456'),(9,'Oscar Uceta','oscarcitord@gmail.com','123456789'),(10,'faustino','favsol26@gmail.com','123456'),(11,'dario','dario@correo.com','123'),(12,'brian','brianalmengo','123'),(13,'yinet jaquez de la cruz','yinet.jaquez@hotmail.com','casa'),(14,'Darvis','darvis@darvis','12345'),(15,'yamely','yamely_da@hotmail.com','12345'),(16,'Joan Manuel Ventura','joanventura@outlook.com','123456'),(17,'','faustayala.95@gmail.com','123456'),(18,'Dario Almonte','dario','123'),(19,'eli','cdwdeli@hotmail.com','klk'),(20,'Melvin','melmarte@hotmail.com','123'),(21,'Melvin','melmarte@hotmail.com','123'),(22,'Melvin','melmarte@hotmail.com','123'),(23,'Melvin','melmarte@hotmail.com','123'),(24,'Angel Cordero','afct99@hotmail.com','java'),(25,'Angel Cordero','afct99@hotmail.com','java'),(26,'','melmarte@hotmail.com','123'),(27,'','',''),(28,'garibaldy','dgaribaldy@gmail.com','12345'),(29,'Melvin','melmarte@hotmail.com','123'),(30,'JUANA','juana@outlook.com','123456'),(31,'','juan@juan','asd');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-17 12:52:01
