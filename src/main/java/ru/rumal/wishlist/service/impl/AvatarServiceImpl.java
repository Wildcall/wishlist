package ru.rumal.wishlist.service.impl;

import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.AvatarService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

public class AvatarServiceImpl implements AvatarService {
    public final String BASE64_PREFIX = "data:image/png;base64,";

    public void setWidth(int width) {
        if (width < 1) throw new IllegalArgumentException("Width must be greater than 1");
        this.width = width;
    }

    public void setGrid(int grid) {
        if (grid < 1) throw new IllegalArgumentException("Grid must be greater than 1");
        this.grid = grid;
    }
    public int width = 20;
    public int grid = 5;

    @Override
    public String generate(User user) {
        return BASE64_PREFIX + new String(Base64.getEncoder().encode(create()));
    }

    private byte[] create() {
        int padding = width / 2;
        int size = width * grid + width;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D _2d = img.createGraphics();
        _2d.setColor(new Color(240, 240, 240));
        _2d.fillRect(0, 0, size, size);
        _2d.setColor(randomColor(80, 200));
        Random random = new Random();
        for (int x = 0; x < Math.ceil(grid  /2.); x++) {
            for (int y = 0; y < grid; y++) {
                if (random.nextInt(10) < 6) {
                    _2d.fillRect((padding + x * width), (padding + y * width), width, width);
                    _2d.fillRect((padding + ((grid - 1) - x) * width), (padding + y * width), width, width);
                }
            }
        }
        _2d.dispose();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private Color randomColor(int min, int max) {
        Random random = new Random();
        int r = min + random.nextInt(Math.abs(max - min));
        int g = min + random.nextInt(Math.abs(max - min));
        int b = min + random.nextInt(Math.abs(max - min));
        return new Color(r, g, b);
    }

}

