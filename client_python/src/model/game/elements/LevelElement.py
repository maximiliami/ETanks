import os

import pygame as pg

from model.game.elements.LevelElementType import LevelElementType


class LevelElement(pg.sprite.Sprite):
    def __init__(self, imagePath, positionX, positionY, width, height, rotation):
        pg.sprite.Sprite.__init__(self)

        self.type = LevelElementType
        self.imagPath = imagePath
        self.positionX = positionX
        self.positionY = positionY
        self.width = width
        self.height = height
        self.rotation = rotation

