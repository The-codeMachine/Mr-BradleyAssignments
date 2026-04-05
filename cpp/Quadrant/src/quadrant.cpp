#include "quadrant.hpp"

Quadrant::Quadrant(int initValue) {
    data = initValue;
}

int Quadrant::klingons() {
    return data / 100;
}

int Quadrant::bases() {
    return (data % 100) / 10;
}

int Quadrant::stars() {
    return (data % 100) % 10;
}