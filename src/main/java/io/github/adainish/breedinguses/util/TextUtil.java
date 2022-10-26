package io.github.adainish.breedinguses.util;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    private static final Pattern HEXPATTERN = Pattern.compile("\\{(#[a-fA-F0-9]{6})}");
    private static final String SPLITPATTERN = "((?=\\{#[a-fA-F0-9]{6}}))";

    public static ITextComponent parseHexCodes(String text, boolean removeItalics) {
        if(text == null)
            return null;
        StringTextComponent comp = new StringTextComponent("");

        String[] temp = text.split(SPLITPATTERN);
        Arrays.stream(temp).forEach(s -> {
            Matcher m = HEXPATTERN.matcher(s);
            if(m.find()) {
                Color color = Color.fromHex(m.group(1));
                s = m.replaceAll("");
                if(removeItalics)
                    comp.appendSibling(new StringTextComponent(s).setStyle(Style.EMPTY.setColor(color).setItalic(false)));
                else
                    comp.appendSibling(new StringTextComponent(s).setStyle(Style.EMPTY.setColor(color)));
            } else {
                comp.appendSibling(new StringTextComponent(s));
            }
        });

        return comp;
    }
}
