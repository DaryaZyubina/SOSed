package ru.nsk.nsu.sosed.data;

public interface IMapper<From, To> {
    To map(From from);
}