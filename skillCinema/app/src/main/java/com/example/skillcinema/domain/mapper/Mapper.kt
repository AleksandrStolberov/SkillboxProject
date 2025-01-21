package com.example.skillcinema.domain.mapper

abstract class Mapper<From, To> {
    abstract suspend fun map(from: From): To
}