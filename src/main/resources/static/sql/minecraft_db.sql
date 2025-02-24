-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-02-2025 a las 20:16:06
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `minecraft_db`
--
CREATE DATABASE IF NOT EXISTS `minecraft_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `minecraft_db`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventarios`
--

CREATE TABLE `inventarios` (
  `id` bigint(20) NOT NULL,
  `capacidad` int(11) NOT NULL,
  `items` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `inventarios`
--

INSERT INTO `inventarios` (`id`, `capacidad`, `items`) VALUES
(1, 100, 'Espada de diamante, Manzana dorada'),
(2, 100, 'Arco, Flechas, Pociones'),
(3, 100, 'Pico de netherite, Antorchas, Bloques de construcción, Pico de madera, TNT'),
(4, 100, 'Mena de hierro, Pico de piedra, Trigo, Mena de hierro, Trigo, Zanahoria, Poción de fuerza, Carbón, Lingote de oro, Mesa de trabajo, Yunque'),
(6, 100, 'Diamante, Oro, Pan'),
(7, 100, 'Piedra'),
(8, 100, 'Espada de madera, Antorcha, Trigo, Mena de diamante, Horno, Trigo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jugadores`
--

CREATE TABLE `jugadores` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `nivel` int(11) DEFAULT 1,
  `inventario_id` bigint(20) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jugadores`
--

INSERT INTO `jugadores` (`id`, `nombre`, `nivel`, `inventario_id`, `usuario_id`) VALUES
(5, 'Steve', 10, 1, 10),
(6, 'Alex', 12, 2, 6),
(7, 'Herobrine', 99, 3, 5),
(10, 'Pepe', 7, 4, 3),
(12, 'Alba', 15, 6, 12),
(13, 'Juan', 4, 7, 13),
(14, 'Yo', 1, 8, 14);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `username`, `password`, `rol`) VALUES
(3, 'Pepe', '$2a$10$t8x4XbV6LEGMUxVgZszXIOfHn2FR0Aevl9rOWchdNfRIIjZwu7R96', 'USER'),
(4, 'root', '$2a$10$ELJ.FbM6BBqYJI3nUVMP/.4pJalToikKYcWn4OhtgXjPvN2Em5mHy', 'ADMIN'),
(5, 'Herobrine', '$2a$10$NxPR8u55VSN4sWRycUm69elfMr2WhLtSo7ey/7Yc3rLFycfzHhEei', 'USER'),
(6, 'Alex', '$2a$10$lugKfG.mQx1ts6tSfYtuJO5eFCLFGDZHFSar/b.goVfDMfB9imXXm', 'USER'),
(10, 'Steve', '$2a$10$5h.wq/v3xmDBPIkLMbhtZO2R4xSXzEUDodpj5OpOaFnQdlL0HoA16', 'USER'),
(12, 'Alba', '$2a$10$FIQMZ.b0lTNqLNDBC7hhY.x5uIRu2QNTEzc41ice6BDjBb8YZLauS', 'USER'),
(13, 'Juan', '$2a$10$ryRM9/YLbKXUaPaAMWgyK.1xSaSR43fmcC10eLTYaDZBuYXZBI4vC', 'USER'),
(14, 'Yo', '$2a$10$jO8kD4I6ADHzCqxqxgDG6OObVHNYAwUUoNuPUMC5xY1Zduvh6osX6', 'USER');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `inventarios`
--
ALTER TABLE `inventarios`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `jugadores`
--
ALTER TABLE `jugadores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `inventario_id` (`inventario_id`),
  ADD KEY `fk_jugador_usuario` (`usuario_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `inventarios`
--
ALTER TABLE `inventarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `jugadores`
--
ALTER TABLE `jugadores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `jugadores`
--
ALTER TABLE `jugadores`
  ADD CONSTRAINT `fk_jugador_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jugadores_ibfk_1` FOREIGN KEY (`inventario_id`) REFERENCES `inventarios` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
