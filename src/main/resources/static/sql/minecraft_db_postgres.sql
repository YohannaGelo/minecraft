-- phpPgAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-02-2025 a las 20:16:06
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET timezone = 'UTC';
BEGIN;

-- Base de datos: minecraft_db
-- CREATE DATABASE no es necesario en PostgreSQL si ya la has creado en el panel de Render.
-- Se usará directamente la base existente.

-- --------------------------------------------------------

-- Estructura de la tabla `inventarios`
CREATE TABLE IF NOT EXISTS "inventarios" (
  "id" SERIAL PRIMARY KEY,
  "capacidad" INTEGER NOT NULL,
  "items" VARCHAR(255) DEFAULT NULL
);

-- Volcado de datos para la tabla `inventarios`
INSERT INTO "inventarios" ("capacidad", "items") VALUES
(100, 'Espada de diamante, Manzana dorada'),
(100, 'Arco, Flechas, Pociones'),
(100, 'Pico de netherite, Antorchas, Bloques de construcción, Pico de madera, TNT'),
(100, 'Mena de hierro, Pico de piedra, Trigo, Mena de hierro, Trigo, Zanahoria, Poción de fuerza, Carbón, Lingote de oro, Mesa de trabajo, Yunque'),
(100, 'Diamante, Oro, Pan'),
(100, 'Piedra'),
(100, 'Espada de madera, Antorcha, Trigo, Mena de diamante, Horno, Trigo');

-- --------------------------------------------------------

-- Estructura de la tabla `jugadores`
CREATE TABLE IF NOT EXISTS "jugadores" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR(255) NOT NULL,
  "nivel" INTEGER DEFAULT 1,
  "inventario_id" BIGINT REFERENCES "inventarios"("id") ON DELETE SET NULL,
  "usuario_id" BIGINT REFERENCES "usuarios"("id") ON DELETE CASCADE
);

-- Volcado de datos para la tabla `jugadores`
INSERT INTO "jugadores" ("nombre", "nivel", "inventario_id", "usuario_id") VALUES
('Steve', 10, 1, 10),
('Alex', 12, 2, 6),
('Herobrine', 99, 3, 5),
('Pepe', 7, 4, 3),
('Alba', 15, 6, 12),
('Juan', 4, 7, 13),
('Yo', 1, 8, 14);

-- --------------------------------------------------------

-- Estructura de la tabla `usuarios`
CREATE TABLE IF NOT EXISTS "usuarios" (
  "id" SERIAL PRIMARY KEY,
  "username" VARCHAR(255) NOT NULL UNIQUE,
  "password" VARCHAR(255) NOT NULL,
  "rol" VARCHAR(255) NOT NULL
);

-- Volcado de datos para la tabla `usuarios`
INSERT INTO "usuarios" ("username", "password", "rol") VALUES
('Pepe', '$2a$10$t8x4XbV6LEGMUxVgZszXIOfHn2FR0Aevl9rOWchdNfRIIjZwu7R96', 'USER'),
('root', '$2a$10$ELJ.FbM6BBqYJI3nUVMP/.4pJalToikKYcWn4OhtgXjPvN2Em5mHy', 'ADMIN'),
('Herobrine', '$2a$10$NxPR8u55VSN4sWRycUm69elfMr2WhLtSo7ey/7Yc3rLFycfzHhEei', 'USER'),
('Alex', '$2a$10$lugKfG.mQx1ts6tSfYtuJO5eFCLFGDZHFSar/b.goVfDMfB9imXXm', 'USER'),
('Steve', '$2a$10$5h.wq/v3xmDBPIkLMbhtZO2R4xSXzEUDodpj5OpOaFnQdlL0HoA16', 'USER'),
('Alba', '$2a$10$FIQMZ.b0lTNqLNDBC7hhY.x5uIRu2QNTEzc41ice6BDjBb8YZLauS', 'USER'),
('Juan', '$2a$10$ryRM9/YLbKXUaPaAMWgyK.1xSaSR43fmcC10eLTYaDZBuYXZBI4vC', 'USER'),
('Yo', '$2a$10$jO8kD4I6ADHzCqxqxgDG6OObVHNYAwUUoNuPUMC5xY1Zduvh6osX6', 'USER');

-- --------------------------------------------------------

-- ÍNDICES y restricciones para las tablas
-- Ya se definen dentro de la creación de las tablas mediante la declaración de claves primarias y foráneas.

-- FINALIZAMOS LA TRANSACCIÓN
COMMIT;
