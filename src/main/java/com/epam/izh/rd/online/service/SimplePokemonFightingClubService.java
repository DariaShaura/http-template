package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.entity.Pokemon;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class SimplePokemonFightingClubService implements PokemonFightingClubService {

    PokemonFetchingService pokemonFetchingService;

    public SimplePokemonFightingClubService() {
        pokemonFetchingService = new SimplePokemonFetchingService();
    }

    /**
     * Инициирует бой между двумя покемонами, должен использовать метод doDamage
     *
     * @param p1 атакующий покемон
     * @param p2 защищающийся покемон
     * @return победителя
     */
    public Pokemon doBattle(Pokemon p1, Pokemon p2) {

        Pokemon attacking = p1.getPokemonId() < p2.getPokemonId() ? p1 : p2;

        System.out.format("Бой между %s(id=%d)) и %s(id=%d)). Первым атакует %s.\r\n",
                p1.getPokemonName(), p1.getPokemonId(), p2.getPokemonName(), p2.getPokemonId(), attacking.getPokemonName());

        while ((p1.getHp() > 0) && (p2.getHp() > 0)) {

            Pokemon attacked = p1 == attacking ? p2 : p1;

            short attacked_hp = attacked.getHp();
            doDamage(attacking, attacked);

            System.out.format("Покемон %s атаковал %s (hp = %d).\r\n Hp ( %s ) = %d. \r\n",
                                attacking.getPokemonName(), attacked.getPokemonName(), attacked_hp,  attacked.getPokemonName(), attacked.getHp());
            attacking = attacked;
        }

        Pokemon winner = p1.getHp() > 0 ? p1 : p2;

        System.out.format("Победитель - покемон %s \r\n", winner.getPokemonName());

        return winner;
    }

    /**
     * Метод загружает картинку победителя в корень проекта
     *
     * @param winner победитель
     */
    public void showWinner(Pokemon winner) {

        byte[] bytes_picture = pokemonFetchingService.getPokemonImage(winner.getPokemonName());

        if(bytes_picture == null)
        {
            System.out.println("Картинка победителя не загружена!!!");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes_picture);
        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            ImageIO.write(bufferedImage, "jpg", new File("winner.jpg") );

            System.out.println("Картинка победителя загружена");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод высчитывает урон покемона from и вычитает его из hp покемона to
     *
     * @param from атакующий покемон
     * @param to   защищающийся покемон
     */
    public void doDamage(Pokemon from, Pokemon to) {
        short damage = (short) (from.getAttack() - from.getAttack() * to.getDefense() / 100);

        to.setHp((short) (to.getHp() - damage));
    }
}
