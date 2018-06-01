package forestry.book.gui.elements.encyclopedia.pages;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.item.ItemStack;

import forestry.api.genetics.IAlleleSpecies;
import forestry.api.gui.GuiConstants;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IItemElement;
import forestry.api.gui.ILabelElement;
import forestry.api.gui.ITextElement;
import forestry.api.gui.style.ITextStyle;
import forestry.api.gui.style.TextStyleBuilder;
import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.core.gui.elements.layouts.ElementGroup;
import forestry.core.utils.Translator;

public class EncyclopediaTitlePage extends EncyclopediaPage {
	private static final ITextStyle TITLE_STYLE = new TextStyleBuilder().color(0).underlined(true).build();

	private final ILabelElement title;
	private final IItemElement leftIcon;
	private final IItemElement rightIcon;
	private final ILabelElement scientific;
	private final ILabelElement authorityText;
	private final ILabelElement authority;
	private final ILabelElement complexity;
	private final ITextElement description;
	private final ILabelElement signature;

	public EncyclopediaTitlePage() {
		ElementGroup panel = pane(width, 18);
		title = panel.label("", GuiElementAlignment.MIDDLE_CENTER, TITLE_STYLE);
		leftIcon = panel.item(ItemStack.EMPTY);
		rightIcon = panel.item(ItemStack.EMPTY);
		rightIcon.setAlign(GuiElementAlignment.TOP_RIGHT);
		scientific = label("", GuiElementAlignment.TOP_CENTER, GuiConstants.ITALIC_BLACK_STYLE);
		authorityText = label(Translator.translateToLocal("for.gui.encyclopedia.authority"), GuiElementAlignment.TOP_CENTER, GuiConstants.BLACK_STYLE);
		authority = label("", GuiElementAlignment.TOP_CENTER, GuiConstants.BOLD_BLACK_STYLE);
		setDistance(9);
		complexity = label("", GuiElementAlignment.TOP_CENTER, GuiConstants.BLACK_STYLE);
		description = splitText("", width, GuiConstants.BLACK_STYLE);
		description.setAlign(GuiElementAlignment.TOP_CENTER);
		signature = label("", GuiElementAlignment.TOP_RIGHT, GuiConstants.BLACK_STYLE);
	}

	@Override
	public void updateContent(@Nullable EncyclopediaEntry entry) {
		if(entry != null){
			title.setText(entry.getName());
			leftIcon.setStack(entry.getStack());
			rightIcon.setStack(entry.getStack());
			IAlleleSpecies species = entry.getSpecies();
			scientific.setText(species.getBranch().getScientific() + ' ' + species.getBinomial());
			authority.setText(species.getAuthority());
			complexity.setText(Translator.translateToLocalFormatted("for.gui.encyclopedia.complexity", species.getComplexity()));
			remove(description, signature);
			String desc = species.getDescription();
			if (StringUtils.isBlank(desc) || desc.startsWith("for.description.")) {
				description.setText(Translator.translateToLocal("for.gui.alyzer.nodescription"));
			} else {
				String[] tokens = desc.split("\\|");
				description.setText(tokens[0]);
				if (tokens.length > 1) {
					String sig = "- " + tokens[1];
					signature.setText(sig);
				}
			}
			add(description, signature);
		}else{
			title.setText("");
			leftIcon.setStack(ItemStack.EMPTY);
			rightIcon.setStack(ItemStack.EMPTY);
			scientific.setText("");
			authorityText.setText("");
			authority.setText("");
			complexity.setText("");
			remove(description, signature);
			description.setText("");
			signature.setText("");
			add(description, signature);
		}
	}
}
