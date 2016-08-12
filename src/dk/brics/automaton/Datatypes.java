/*
 * dk.brics.automaton
 * 
 * Copyright (c) 2001-2011 Anders Moeller
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package dk.brics.automaton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Basic automata for representing common datatypes related to Unicode, XML, and
 * XML Schema.
 */
final public class Datatypes {

	private static final Map<String, Automaton> automata;

	private static final Automaton ws;

	private static final Set<String> unicodeblock_names;

	private static final Set<String> unicodecategory_names;

	private static final Set<String> xml_names;

	private static final String[] unicodeblock_names_array = { "BasicLatin", "Latin-1Supplement", "LatinExtended-A",
			"LatinExtended-B", "IPAExtensions", "SpacingModifierLetters", "CombiningDiacriticalMarks", "Greek",
			"Cyrillic", "Armenian", "Hebrew", "Arabic", "Syriac", "Thaana", "Devanagari", "Bengali", "Gurmukhi",
			"Gujarati", "Oriya", "Tamil", "Telugu", "Kannada", "Malayalam", "Sinhala", "Thai", "Lao", "Tibetan",
			"Myanmar", "Georgian", "HangulJamo", "Ethiopic", "Cherokee", "UnifiedCanadianAboriginalSyllabics", "Ogham",
			"Runic", "Khmer", "Mongolian", "LatinExtendedAdditional", "GreekExtended", "GeneralPunctuation",
			"SuperscriptsandSubscripts", "CurrencySymbols", "CombiningMarksforSymbols", "LetterlikeSymbols",
			"NumberForms", "Arrows", "MathematicalOperators", "MiscellaneousTechnical", "ControlPictures",
			"OpticalCharacterRecognition", "EnclosedAlphanumerics", "BoxDrawing", "BlockElements", "GeometricShapes",
			"MiscellaneousSymbols", "Dingbats", "BraillePatterns", "CJKRadicalsSupplement", "KangxiRadicals",
			"IdeographicDescriptionCharacters", "CJKSymbolsandPunctuation", "Hiragana", "Katakana", "Bopomofo",
			"HangulCompatibilityJamo", "Kanbun", "BopomofoExtended", "EnclosedCJKLettersandMonths", "CJKCompatibility",
			"CJKUnifiedIdeographsExtensionA", "CJKUnifiedIdeographs", "YiSyllables", "YiRadicals", "HangulSyllables",
			"CJKCompatibilityIdeographs", "AlphabeticPresentationForms", "ArabicPresentationForms-A",
			"CombiningHalfMarks", "CJKCompatibilityForms", "SmallFormVariants", "ArabicPresentationForms-B", "Specials",
			"HalfwidthandFullwidthForms", "Specials", "OldItalic", "Gothic", "Deseret", "ByzantineMusicalSymbols",
			"MusicalSymbols", "MathematicalAlphanumericSymbols", "CJKUnifiedIdeographsExtensionB",
			"CJKCompatibilityIdeographsSupplement", "Tags" };

	private static final String[] unicodecategory_names_array = { "Lu", "Ll", "Lt", "Lm", "Lo", "L", "Mn", "Mc", "Me",
			"M", "Nd", "Nl", "No", "N", "Pc", "Pd", "Ps", "Pe", "Pi", "Pf", "Po", "P", "Zs", "Zl", "Zp", "Z", "Sm",
			"Sc", "Sk", "So", "S", "Cc", "Cf", "Co", "Cn", "C", "Letter", "Number", "Mark", "Punctuation", "Symbol",
			"Separator", "Other", "_word", "_digit", "_linebreak", "_horizontal", "_vertical", "_space" };

	private static final String[] xml_names_array = { "NCName", "QName", "Char", "NameChar", "URI", "anyname", "noap",
			"whitespace", "whitespacechar", "string", "boolean", "decimal", "float", "integer", "duration", "dateTime",
			"time", "date", "gYearMonth", "gYear", "gMonthDay", "gDay", "hexBinary", "base64Binary", "NCName2",
			"NCNames", "QName2", "Nmtoken2", "Nmtokens", "Name2", "Names", "language" };

	static {
		automata = new HashMap<String, Automaton>();
		ws = Automaton.minimize(Automaton.makeCharSet(" \t\n\r").repeat());
		unicodeblock_names = new HashSet<String>(Arrays.asList(unicodeblock_names_array));
		unicodecategory_names = new HashSet<String>(Arrays.asList(unicodecategory_names_array));
		xml_names = new HashSet<String>(Arrays.asList(xml_names_array));

		if (Automaton.loadAllBuiltins)
			buildAll();
	}

	private Datatypes() {
	}

	/**
	 * Returns pre-built automaton. Automata are constructed when first
	 * requested from the <tt>Datatypes</tt> class, and only need the code in
	 * the jar.
	 * <p>
	 * The following automata are available:
	 * <table border=1> <tr><th>Name</th><th>Description</th></tr>
	 * <tr><td><tt>NCName</tt></td><td><a target="_top" href=
	 * "http://www.w3.org/TR/REC-xml-names/#NT-NCName">
	 * NCName</a> from XML Namespaces 1.0</td>
	 * </tr>
	 * <tr>
	 * <td><tt>QName</tt></td>
	 * <td><a target="_top" href="http://www.w3.org/TR/REC-xml-names/#NT-QName">
	 * QName</a> from XML Namespaces 1.0</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Char</tt></td>
	 * <td><a target="_top" href="http://www.w3.org/TR/REC-xml/#NT-Char">Char
	 * </a> from XML 1.0</td>
	 * </tr>
	 * <tr>
	 * <td><tt>NameChar</tt></td>
	 * <td><a target="_top" href="http://www.w3.org/TR/REC-xml/#NT-NameChar">
	 * NameChar</a> from XML 1.0</td>
	 * </tr>
	 * <tr>
	 * <td><tt>URI</tt></td>
	 * <td><a target="_top" href="http://rfc.net/rfc2396.html#sA%2e">URI</a>
	 * from RFC2396 with amendments from
	 * <a target="_top" href="http://www.faqs.org/rfcs/rfc2373.html">RFC2373
	 * </td>
	 * </tr>
	 * <tr>
	 * <td><tt>anyname</tt></td>
	 * <td>optional URI enclosed by brackets, followed by NCName</td>
	 * </tr>
	 * <tr>
	 * <td><tt>noap</tt></td>
	 * <td>strings not containing '@' and '%'</td>
	 * </tr>
	 * <tr>
	 * <td><tt>whitespace</tt></td>
	 * <td>optional
	 * <a target="_top" href="http://www.w3.org/TR/REC-xml/#NT-S">S</a> from XML
	 * 1.0</td>
	 * </tr>
	 * <tr>
	 * <td><tt>whitespacechar</tt></td>
	 * <td>a single
	 * <a target="_top" href="http://www.w3.org/TR/REC-xml/#NT-S">whitespace
	 * character</a> from XML 1.0</td>
	 * </tr>
	 * <tr>
	 * <td><tt>string</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#string">string</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>boolean</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#boolean">boolean</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>decimal</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#decimal">decimal</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>float</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#float">float</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>integer</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#integer">integer</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>duration</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#duration">duration
	 * </a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>dateTime</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#dateTime">dateTime
	 * </a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>time</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#time">time</a> from
	 * XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>date</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#date">date</a> from
	 * XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>gYearMonth</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#gYearMonth">
	 * gYearMonth</a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>gYear</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#gYear">gYear</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>gMonthDay</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#gMonthDay">gMonthDay
	 * </a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>gDay</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#gDay">gDay</a> from
	 * XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>hexBinary</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#hexBinary">hexBinary
	 * </a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>base64Binary</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#base64Binary">
	 * base64Binary</a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>NCName2</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#NCName">NCName</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>NCNames</tt></td>
	 * <td>list of <a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#NCName">NCName</a>s
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>QName2</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#QName">QName</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Nmtoken2</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#NMTOKEN">NMTOKEN</a>
	 * from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Nmtokens</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#NMTOKENS">NMTOKENS
	 * </a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Name2</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#Name">Name</a> from
	 * XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Names</tt></td>
	 * <td>list of <a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#Name">Name</a>s from
	 * XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>language</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#language">language
	 * </a> from XML Schema Part 2</td>
	 * </tr>
	 * <tr>
	 * <td><tt>BasicLatin</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">BasicLatin</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Latin-1Supplement</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Latin-1Supplement
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>LatinExtended-A</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">LatinExtended-A
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>LatinExtended-B</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">LatinExtended-B
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>IPAExtensions</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">IPAExtensions</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>SpacingModifierLetters</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * SpacingModifierLetters</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CombiningDiacriticalMarks</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CombiningDiacriticalMarks</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Greek</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Greek</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Cyrillic</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Cyrillic</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Armenian</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Armenian</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Hebrew</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Hebrew</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Arabic</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Arabic</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Syriac</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Syriac</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Thaana</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Thaana</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Devanagari</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Devanagari</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Bengali</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Bengali</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Gurmukhi</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Gurmukhi</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Gujarati</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Gujarati</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Oriya</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Oriya</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Tamil</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Tamil</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Telugu</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Telugu</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Kannada</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Kannada</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Malayalam</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Malayalam</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Sinhala</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Sinhala</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Thai</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Thai</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Lao</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Lao</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Tibetan</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Tibetan</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Myanmar</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Myanmar</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Georgian</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Georgian</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>HangulJamo</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">HangulJamo</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Ethiopic</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Ethiopic</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Cherokee</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Cherokee</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>UnifiedCanadianAboriginalSyllabics</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * UnifiedCanadianAboriginalSyllabics</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Ogham</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Ogham</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Runic</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Runic</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Khmer</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Khmer</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Mongolian</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Mongolian</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>LatinExtendedAdditional</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * LatinExtendedAdditional</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>GreekExtended</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">GreekExtended</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>GeneralPunctuation</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * GeneralPunctuation</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>SuperscriptsandSubscripts</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * SuperscriptsandSubscripts</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CurrencySymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">CurrencySymbols
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CombiningMarksforSymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CombiningMarksforSymbols</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>LetterlikeSymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">LetterlikeSymbols
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>NumberForms</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">NumberForms</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Arrows</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Arrows</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>MathematicalOperators</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * MathematicalOperators</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>MiscellaneousTechnical</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * MiscellaneousTechnical</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>ControlPictures</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">ControlPictures
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>OpticalCharacterRecognition</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * OpticalCharacterRecognition</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>EnclosedAlphanumerics</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * EnclosedAlphanumerics</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>BoxDrawing</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">BoxDrawing</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>BlockElements</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">BlockElements</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>GeometricShapes</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">GeometricShapes
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>MiscellaneousSymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * MiscellaneousSymbols</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Dingbats</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Dingbats</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>BraillePatterns</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">BraillePatterns
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKRadicalsSupplement</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKRadicalsSupplement</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>KangxiRadicals</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">KangxiRadicals
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>IdeographicDescriptionCharacters</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * IdeographicDescriptionCharacters</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKSymbolsandPunctuation</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKSymbolsandPunctuation</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Hiragana</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Hiragana</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Katakana</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Katakana</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Bopomofo</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Bopomofo</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>HangulCompatibilityJamo</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * HangulCompatibilityJamo</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Kanbun</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Kanbun</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>BopomofoExtended</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">BopomofoExtended
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>EnclosedCJKLettersandMonths</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * EnclosedCJKLettersandMonths</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKCompatibility</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">CJKCompatibility
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKUnifiedIdeographsExtensionA</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKUnifiedIdeographsExtensionA</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKUnifiedIdeographs</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKUnifiedIdeographs</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>YiSyllables</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">YiSyllables</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>YiRadicals</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">YiRadicals</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>HangulSyllables</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">HangulSyllables
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKCompatibilityIdeographs</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKCompatibilityIdeographs</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>AlphabeticPresentationForms</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * AlphabeticPresentationForms</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>ArabicPresentationForms-A</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * ArabicPresentationForms-A</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CombiningHalfMarks</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CombiningHalfMarks</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKCompatibilityForms</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKCompatibilityForms</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>SmallFormVariants</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">SmallFormVariants
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>ArabicPresentationForms-B</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * ArabicPresentationForms-B</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Specials</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Specials</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>HalfwidthandFullwidthForms</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * HalfwidthandFullwidthForms</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Specials</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Specials</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>OldItalic</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">OldItalic</a>
	 * block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Gothic</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Gothic</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Deseret</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Deseret</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>ByzantineMusicalSymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * ByzantineMusicalSymbols</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>MusicalSymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">MusicalSymbols
	 * </a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>MathematicalAlphanumericSymbols</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * MathematicalAlphanumericSymbols</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKUnifiedIdeographsExtensionB</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKUnifiedIdeographsExtensionB</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>CJKCompatibilityIdeographsSupplement</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">
	 * CJKCompatibilityIdeographsSupplement</a> block from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Tags</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/Blocks-4.txt">Tags</a> block
	 * from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Lu</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Lu</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Ll</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Ll</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Lt</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Lt</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Lm</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Lm</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Lo</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Lo</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>L</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">L</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Mn</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Mn</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Mc</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Mc</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Me</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Me</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>M</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">M</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Nd</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Nd</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Nl</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Nl</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>No</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">No</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>N</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">N</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Pc</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Pc</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Pd</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Pd</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Ps</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Ps</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Pe</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Pe</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Pi</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Pi</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Pf</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Pf</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Po</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Po</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>P</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">P</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Zs</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Zs</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Zl</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Zl</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Zp</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Zp</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Z</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Z</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Sm</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Sm</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Sc</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Sc</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Sk</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Sk</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>So</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">So</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>S</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">S</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Cc</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Cc</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Cf</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Cf</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Co</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Co</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>Cn</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">Cn</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>C</tt></td>
	 * <td><a target="_top" href=
	 * "http://www.unicode.org/Public/3.1-Update/UnicodeData-3.1.0.txt">C</a>
	 * category from Unicode 3.1</td>
	 * </tr>
	 * <tr>
	 * <td><tt>_linebreak</tt></td>
	 * <td>Equivalent to Java Pattern's \R escape and accessible with \R; either
	 * a single-char linebreak or the \r\n digraph.</td>
	 * </tr>
	 * <tr>
	 * <td><tt>_word</tt></td>
	 * <td>Merger of Unicode categories L, Mc, and Mn with the digits 0-9 and
	 * the underscore. Accessible via \w .</td>
	 * </tr>
	 * <tr>
	 * <td><tt>_digit</tt></td>
	 * <td>Only the digits 0-9. Accessible via \d .</td>
	 * </tr>
	 * <tr>
	 * <td><tt>_horizontal</tt></td>
	 * <td>Horizontal whitespace, equivalent to Java Pattern's \h escape.
	 * Accessible via \h .</td>
	 * </tr>
	 * <tr>
	 * <td><tt>_vertical</tt></td>
	 * <td>Vertical whitespace, equivalent to Java Pattern's \v escape.
	 * Accessible via \v .</td>
	 * </tr>
	 * <tr>
	 * <td><tt>_space</tt></td>
	 * <td>Common subset of whitespace chars, equivalent to Java Pattern's \s
	 * escape. Accessible via \s .</td>
	 * </tr>
	 * </table>
	 * <p>
	 * Loaded automata are cached in memory.
	 * 
	 * @param name
	 *            name of automaton
	 * @return automaton
	 */
	public static Automaton get(String name) {

		Automaton a = automata.get(name);
		if (a == null && !Automaton.loadAllBuiltins) {
			synchronized (automata) {
				a = automata.get(name);
				if (a == null) {
					a = load(name);
					automata.put(name, a);
				}
			}
			return a;
		}
		return a;
	}

	/**
	 * Checks whether the given string is the name of a Unicode block (see
	 * {@link #get(String)}).
	 */
	public static boolean isUnicodeBlockName(String name) {
		return unicodeblock_names.contains(name);
	}

	/**
	 * Checks whether the given string is the name of a Unicode category (see
	 * {@link #get(String)}).
	 */
	public static boolean isUnicodeCategoryName(String name) {
		return unicodecategory_names.contains(name);
	}

	/**
	 * Checks whether the given string is the name of an XML / XML Schema
	 * automaton (see {@link #get(String)}).
	 */
	public static boolean isXMLName(String name) {
		return xml_names.contains(name);
	}

	public static Automaton getWhitespaceAutomaton() {
		return ws;
	}

	private static void put(Map<String, Automaton> map, String name, Automaton a) {
		map.put(name, a);
		// System.out.println(" " + name + ": " + a.getNumberOfStates() + "
		// states, " + a.getNumberOfTransitions() + " transitions");
	}

	////

	private static Automaton getAut1() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFB00', '\uFB4F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut2() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0600', '\u06FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut3() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFB50', '\uFDFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut4() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFE70', '\uFEFE', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut5() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0530', '\u058F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut6() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2190', '\u21FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut7() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0000', '\u007F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut8() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0980', '\u09FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut9() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2580', '\u259F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut10() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3100', '\u312F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut11() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u31A0', '\u31BF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut12() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2500', '\u257F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut13() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2800', '\u28FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut14() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDCFF', state2));
		state1.alter(false, 0, new Transition('\uD834', '\uD834', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut15() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		state0.alter(false, 0, new Transition('\uD801', '\uD801', state14), new Transition('\uD800', '\uD800', state2),
				new Transition('\uDC00', '\uDC00', state2), new Transition('\uD800', '\uD800', state10),
				new Transition('\u04CD', '\u04CF', state18), new Transition('\u0B34', '\u0B35', state18),
				new Transition('\uFE67', '\uFE67', state18), new Transition('\u2596', '\u259F', state18),
				new Transition('\u0C04', '\u0C04', state18), new Transition('\uFB37', '\uFB37', state18),
				new Transition('\u0CCE', '\u0CD4', state18), new Transition('\uFE6C', '\uFE6F', state18),
				new Transition('\u0B3A', '\u0B3B', state18), new Transition('\u066E', '\u066F', state18),
				new Transition('\u05A2', '\u05A2', state18), new Transition('\uFB3D', '\uFB3D', state18),
				new Transition('\u0E5C', '\u0E80', state18), new Transition('\u11A3', '\u11A7', state18),
				new Transition('\uFE73', '\uFE73', state18), new Transition('\uFB3F', '\uFB3F', state18),
				new Transition('\u180B', '\u180E', state2), new Transition('\u0C0D', '\u0C0D', state18),
				new Transition('\uDB40', '\uDB40', state3), new Transition('\uDB40', '\uDB40', state17),
				new Transition('\u200C', '\u200F', state2), new Transition('\u0CD7', '\u0CDD', state18),
				new Transition('\u180F', '\u180F', state18), new Transition('\u09A9', '\u09A9', state18),
				new Transition('\uFE75', '\uFE75', state18), new Transition('\uFB42', '\uFB42', state18),
				new Transition('\u0C11', '\u0C11', state18), new Transition('\u0B44', '\u0B46', state18),
				new Transition('\u0CDF', '\u0CDF', state18), new Transition('\u0000', '\u001F', state2),
				new Transition('\uFB45', '\uFB45', state18), new Transition('\u16F1', '\u177F', state18),
				new Transition('\u1F46', '\u1F47', state18), new Transition('\u1347', '\u1347', state18),
				new Transition('\u0F48', '\u0F48', state18), new Transition('\u0A75', '\u0A80', state18),
				new Transition('\u1677', '\u167F', state18), new Transition('\u0B49', '\u0B4A', state18),
				new Transition('\u327C', '\u327E', state18), new Transition('\u0CE2', '\u0CE5', state18),
				new Transition('\u09B1', '\u09B1', state18), new Transition('\u0DB2', '\u0DB2', state18),
				new Transition('\u274C', '\u274C', state18), new Transition('\u09B3', '\u09B5', state18),
				new Transition('\u274E', '\u274E', state18), new Transition('\u1F4E', '\u1F4F', state18),
				new Transition('\u0E83', '\u0E83', state18), new Transition('\u181A', '\u181F', state18),
				new Transition('\u0A84', '\u0A84', state18), new Transition('\uD802', '\uD833', state8),
				new Transition('\u0B4E', '\u0B55', state18), new Transition('\u0E85', '\u0E86', state18),
				new Transition('\u05BA', '\u05BA', state18), new Transition('\u1287', '\u1287', state18),
				new Transition('\u09BA', '\u09BB', state18), new Transition('\u2753', '\u2755', state18),
				new Transition('\u1022', '\u1022', state18), new Transition('\u0DBC', '\u0DBC', state18),
				new Transition('\u0E89', '\u0E89', state18), new Transition('\u1289', '\u1289', state18),
				new Transition('\u09BD', '\u09BD', state18), new Transition('\u2757', '\u2757', state18),
				new Transition('\u1F58', '\u1F58', state18), new Transition('\u0DBE', '\u0DBF', state18),
				new Transition('\u0E8B', '\u0E8C', state18), new Transition('\u034F', '\u035F', state18),
				new Transition('\u0A8C', '\u0A8C', state18), new Transition('\u0B58', '\u0B5B', state18),
				new Transition('\u1F5A', '\u1F5A', state18), new Transition('\u20E4', '\u20FF', state18),
				new Transition('\u0A8E', '\u0A8E', state18), new Transition('\u1028', '\u1028', state18),
				new Transition('\u128E', '\u128F', state18), new Transition('\u1F5C', '\u1F5C', state18),
				new Transition('\u0C29', '\u0C29', state18), new Transition('\u04F6', '\u04F7', state18),
				new Transition('\u24EB', '\u24FF', state18), new Transition('\u0E8E', '\u0E93', state18),
				new Transition('\u0B5E', '\u0B5E', state18), new Transition('\u135B', '\u1360', state18),
				new Transition('\u1F5E', '\u1F5E', state18), new Transition('\u102B', '\u102B', state18),
				new Transition('\u0A92', '\u0A92', state18), new Transition('\uFF5F', '\uFF60', state18),
				new Transition('\u09C5', '\u09C6', state18), new Transition('\u10F7', '\u10FA', state18),
				new Transition('\u202A', '\u202E', state2), new Transition('\u275F', '\u2760', state18),
				new Transition('\u0CF0', '\u0D01', state18), new Transition('\u0DC7', '\u0DC9', state18),
				new Transition('\u09C9', '\u09CA', state18), new Transition('\u0B62', '\u0B65', state18),
				new Transition('\u10FC', '\u10FF', state18), new Transition('\u05C5', '\u05CF', state18),
				new Transition('\u0E98', '\u0E98', state18), new Transition('\uDB41', '\uDB7F', state8),
				new Transition('\uF8FF', '\uF8FF', state2), new Transition('\uD834', '\uD834', state4),
				new Transition('\u0DCB', '\u0DCE', state18), new Transition('\uD834', '\uD834', state12),
				new Transition('\u2E9A', '\u2E9A', state18), new Transition('\u0C34', '\u0C34', state18),
				new Transition('\u1033', '\u1035', state18), new Transition('\u30FF', '\u3104', state18),
				new Transition('\u2427', '\u243F', state18), new Transition('\uD835', '\uD835', state13),
				new Transition('\u074B', '\u077F', state18), new Transition('\u0904', '\u0904', state18),
				new Transition('\u0D04', '\u0D04', state18), new Transition('\u1E9C', '\u1E9F', state18),
				new Transition('\u169D', '\u169F', state18), new Transition('\u09CE', '\u09D6', state18),
				new Transition('\u0363', '\u0373', state18), new Transition('\u3401', '\u4DB4', state18),
				new Transition('\u0EA0', '\u0EA0', state18), new Transition('\u0F6B', '\u0F70', state18),
				new Transition('\u0DD5', '\u0DD5', state18), new Transition('\u0C3A', '\u0C3D', state18),
				new Transition('\uD836', '\uD83F', state8), new Transition('\u303B', '\u303D', state18),
				new Transition('\u2768', '\u2775', state18), new Transition('\u103A', '\u103F', state18),
				new Transition('\u0DD7', '\u0DD7', state18), new Transition('\u0EA4', '\u0EA4', state18),
				new Transition('\u0EA6', '\u0EA6', state18), new Transition('\u3040', '\u3040', state18),
				new Transition('\u09D8', '\u09DB', state18), new Transition('\u0D0D', '\u0D0D', state18),
				new Transition('\uD840', '\uD840', state16), new Transition('\u0EA8', '\u0EA9', state18),
				new Transition('\u0AA9', '\u0AA9', state18), new Transition('\u0D11', '\u0D11', state18),
				new Transition('\u09DE', '\u09DE', state18), new Transition('\u0376', '\u0379', state18),
				new Transition('\u0C45', '\u0C45', state18), new Transition('\uFDC8', '\uFDEF', state18),
				new Transition('\u0EAC', '\u0EAC', state18), new Transition('\u3377', '\u337A', state18),
				new Transition('\uFA2E', '\uFAFF', state18), new Transition('\u2047', '\u2047', state18),
				new Transition('\u0B71', '\u0B81', state18), new Transition('\u4DB6', '\u4DFF', state18),
				new Transition('\u02AE', '\u02AF', state18), new Transition('\u12AF', '\u12AF', state18),
				new Transition('\u237C', '\u237C', state18), new Transition('\u037B', '\u037D', state18),
				new Transition('\u0C49', '\u0C49', state18), new Transition('\u31B8', '\u31FF', state18),
				new Transition('\u0AB1', '\u0AB1', state18), new Transition('\u12B1', '\u12B1', state18),
				new Transition('\u09E4', '\u09E5', state18), new Transition('\u1F7E', '\u1F7F', state18),
				new Transition('\u2900', '\u2E7F', state18), new Transition('\uDB7F', '\uDB7F', state2),
				new Transition('\uDB80', '\uDB80', state1), new Transition('\u04FA', '\u0530', state18),
				new Transition('\u0AB4', '\u0AB4', state18), new Transition('\uDB80', '\uDB80', state16),
				new Transition('\u037F', '\u0383', state18), new Transition('\uA4C7', '\uABFF', state18),
				new Transition('\u12B6', '\u12B7', state18), new Transition('\u0DE0', '\u0DF1', state18),
				new Transition('\u0B84', '\u0B84', state18), new Transition('\u0C4E', '\u0C54', state18),
				new Transition('\u32B1', '\u32BF', state18), new Transition('\u0EBA', '\u0EBA', state18),
				new Transition('\u05EB', '\u05EF', state18), new Transition('\u0ABA', '\u0ABB', state18),
				new Transition('\u244B', '\u245F', state18), new Transition('\u038B', '\u038B', state18),
				new Transition('\u0EBE', '\u0EBF', state18), new Transition('\u12BF', '\u12BF', state18),
				new Transition('\u0B8B', '\u0B8D', state18), new Transition('\uD841', '\uD868', state8),
				new Transition('\u038D', '\u038D', state18), new Transition('\u0F8C', '\u0F8F', state18),
				new Transition('\u12C1', '\u12C1', state18), new Transition('\u0C57', '\u0C5F', state18),
				new Transition('\u0D29', '\u0D29', state18), new Transition('\u0B91', '\u0B91', state18),
				new Transition('\u137D', '\u139F', state18), new Transition('\u204E', '\u2069', state18),
				new Transition('\u0EC5', '\u0EC5', state18), new Transition('\u0AC6', '\u0AC6', state18),
				new Transition('\u12C6', '\u12C7', state18), new Transition('\u0EC7', '\u0EC7', state18),
				new Transition('\u2672', '\u2700', state18), new Transition('\u21F4', '\u21FF', state18),
				new Transition('\u0DF5', '\u0E00', state18), new Transition('\u312D', '\u3130', state18),
				new Transition('\u25F8', '\u25FF', state18), new Transition('\u2795', '\u2797', state18),
				new Transition('\u11FA', '\u11FF', state18), new Transition('\u0ACA', '\u0ACA', state18),
				new Transition('\u0C62', '\u0C65', state18), new Transition('\u0B96', '\u0B98', state18),
				new Transition('\u0F98', '\u0F98', state18), new Transition('\u09FB', '\u0A01', state18),
				new Transition('\u32CC', '\u32CF', state18), new Transition('\u0B9B', '\u0B9B', state18),
				new Transition('\uD869', '\uD869', state15), new Transition('\u0ACE', '\u0ACF', state18),
				new Transition('\u0ECE', '\u0ECF', state18), new Transition('\u05F5', '\u060B', state18),
				new Transition('\u12CF', '\u12CF', state18), new Transition('\u0B9D', '\u0B9D', state18),
				new Transition('\u0A03', '\u0A04', state18), new Transition('\u206A', '\u206F', state2),
				new Transition('\u1207', '\u1207', state18), new Transition('\u093A', '\u093B', state18),
				new Transition('\u0BA0', '\u0BA2', state18), new Transition('\u03A2', '\u03A2', state18),
				new Transition('\u0D3A', '\u0D3D', state18), new Transition('\u12D7', '\u12D7', state18),
				new Transition('\uDB81', '\uDBBE', state8), new Transition('\u2071', '\u2073', state18),
				new Transition('\u0AD1', '\u0ADF', state18), new Transition('\u0BA5', '\u0BA7', state18),
				new Transition('\u0A0B', '\u0A0E', state18), new Transition('\u0EDA', '\u0EDB', state18),
				new Transition('\uD86A', '\uD87D', state8), new Transition('\uFDFC', '\uFE1F', state18),
				new Transition('\u0D44', '\u0D45', state18), new Transition('\u0A11', '\u0A12', state18),
				new Transition('\u0BAB', '\u0BAD', state18), new Transition('\u07B1', '\u0900', state18),
				new Transition('\u0C70', '\u0C81', state18), new Transition('\u060D', '\u061A', state18),
				new Transition('\uFD40', '\uFD4F', state18), new Transition('\u213B', '\u2152', state18),
				new Transition('\u0D49', '\u0D49', state18), new Transition('\u1878', '\u187F', state18),
				new Transition('\u2614', '\u2618', state18), new Transition('\u27B0', '\u27B0', state18),
				new Transition('\u0AE1', '\u0AE5', state18), new Transition('\uD87E', '\uD87E', state11),
				new Transition('\u094E', '\u094F', state18), new Transition('\u1FB5', '\u1FB5', state18),
				new Transition('\u0BB6', '\u0BB6', state18), new Transition('\u105A', '\u109F', state18),
				new Transition('\u061C', '\u061E', state18), new Transition('\u0C84', '\u0C84', state18),
				new Transition('\u321D', '\u321F', state18), new Transition('\u0D4E', '\u0D56', state18),
				new Transition('\u0620', '\u0620', state18), new Transition('\u0220', '\u0221', state18),
				new Transition('\u0487', '\u0487', state18), new Transition('\u06EE', '\u06EF', state18),
				new Transition('\u0BBA', '\u0BBD', state18), new Transition('\u12EF', '\u12EF', state18),
				new Transition('\u0955', '\u0957', state18), new Transition('\u0FBD', '\u0FBD', state18),
				new Transition('\u048A', '\u048B', state18), new Transition('\u0557', '\u0558', state18),
				new Transition('\u0EDE', '\u0EFF', state18), new Transition('\uDBBF', '\uDBBF', state7),
				new Transition('\uD87F', '\uDB3F', state8), new Transition('\uDBBF', '\uDBBF', state9),
				new Transition('\u0C8D', '\u0C8D', state18), new Transition('\uDBC0', '\uDBC0', state6),
				new Transition('\uA48D', '\uA48F', state18), new Transition('\uDBC0', '\uDBC0', state16),
				new Transition('\uFFBF', '\uFFC1', state18), new Transition('\u0A29', '\u0A29', state18),
				new Transition('\u0D58', '\u0D5F', state18), new Transition('\u115A', '\u115E', state18),
				new Transition('\uFE24', '\uFE2F', state18), new Transition('\u0BC3', '\u0BC5', state18),
				new Transition('\u0C91', '\u0C91', state18), new Transition('\u1FC5', '\u1FC5', state18),
				new Transition('\u007F', '\u009F', state2), new Transition('\u02EF', '\u02FF', state18),
				new Transition('\uFBB2', '\uFBD2', state18), new Transition('\u0560', '\u0560', state18),
				new Transition('\u0AF0', '\u0B00', state18), new Transition('\u22F2', '\u22FF', state18),
				new Transition('\u2EF4', '\u2EFF', state18), new Transition('\uAC01', '\uD7A2', state18),
				new Transition('\u4E01', '\u9FA4', state18), new Transition('\u0BC9', '\u0BC9', state18),
				new Transition('\uFFC8', '\uFFC9', state18), new Transition('\uFEFD', '\uFEFE', state18),
				new Transition('\u1EFA', '\u1EFF', state18), new Transition('\u3095', '\u3098', state18),
				new Transition('\u0D62', '\u0D65', state18), new Transition('\u0A31', '\u0A31', state18),
				new Transition('\uE001', '\uF8FE', state18), new Transition('\u06FF', '\u06FF', state18),
				new Transition('\u208F', '\u209F', state18), new Transition('\u32FF', '\u32FF', state18),
				new Transition('\uFEFF', '\uFEFF', state2), new Transition('\u0A34', '\u0A34', state18),
				new Transition('\u0FCD', '\u0FCE', state18), new Transition('\uFF00', '\uFF00', state18),
				new Transition('\u03CF', '\u03CF', state18), new Transition('\u0A37', '\u0A37', state18),
				new Transition('\u0B04', '\u0B04', state18), new Transition('\uFFD0', '\uFFD1', state18),
				new Transition('\u2705', '\u2705', state18), new Transition('\u0BCE', '\u0BD6', state18),
				new Transition('\u309F', '\u30A0', state18), new Transition('\u0A3A', '\u0A3B', state18),
				new Transition('\u1FD4', '\u1FD5', state18), new Transition('\uA4A2', '\uA4A3', state18),
				new Transition('\u0E3B', '\u0E3E', state18), new Transition('\u0A3D', '\u0A3D', state18),
				new Transition('\u063B', '\u063F', state18), new Transition('\u239B', '\u23FF', state18),
				new Transition('\u270A', '\u270B', state18), new Transition('\u03D8', '\u03D9', state18),
				new Transition('\uFFD8', '\uFFD9', state18), new Transition('\uFB07', '\uFB12', state18),
				new Transition('\uD7A4', '\uD7FF', state18), new Transition('\u0B0D', '\u0B0E', state18),
				new Transition('\u070E', '\u070E', state18), new Transition('\u9FA6', '\u9FFF', state18),
				new Transition('\u070F', '\u070F', state2), new Transition('\u130F', '\u130F', state18),
				new Transition('\u1FDC', '\u1FDC', state18), new Transition('\u0CA9', '\u0CA9', state18),
				new Transition('\u18AA', '\u1DFF', state18), new Transition('\uFFDD', '\uFFDF', state18),
				new Transition('\u0234', '\u024F', state18), new Transition('\u1311', '\u1311', state18),
				new Transition('\u17DD', '\u17DF', state18), new Transition('\u0A43', '\u0A46', state18),
				new Transition('\u0B11', '\u0B12', state18), new Transition('\u33DE', '\u33DF', state18),
				new Transition('\u0971', '\u0980', state18), new Transition('\u0BD8', '\u0BE6', state18),
				new Transition('\u0D70', '\u0D81', state18), new Transition('\u1247', '\u1247', state18),
				new Transition('\uFE45', '\uFE48', state18), new Transition('\u1249', '\u1249', state18),
				new Transition('\u0A49', '\u0A4A', state18), new Transition('\u1316', '\u1317', state18),
				new Transition('\u1F16', '\u1F17', state18), new Transition('\uDBC1', '\uDBFE', state8),
				new Transition('\u2FD6', '\u2FEF', state18), new Transition('\u27BF', '\u27FF', state18),
				new Transition('\uFB18', '\uFB1C', state18), new Transition('\u0CB4', '\u0CB4', state18),
				new Transition('\uA4B4', '\uA4B4', state18), new Transition('\uFFE7', '\uFFE7', state18),
				new Transition('\u124E', '\u124F', state18), new Transition('\u0984', '\u0984', state18),
				new Transition('\u0D84', '\u0D84', state18), new Transition('\u1F1E', '\u1F1F', state18),
				new Transition('\u131F', '\u131F', state18), new Transition('\uFE53', '\uFE53', state18),
				new Transition('\u0FD0', '\u0FFF', state18), new Transition('\u0A4E', '\u0A58', state18),
				new Transition('\u3244', '\u325F', state18), new Transition('\u0588', '\u0588', state18),
				new Transition('\u0CBA', '\u0CBD', state18), new Transition('\u1257', '\u1257', state18),
				new Transition('\u1FF0', '\u1FF1', state18), new Transition('\u2184', '\u218F', state18),
				new Transition('\u1259', '\u1259', state18), new Transition('\uFFEF', '\uFFF8', state18),
				new Transition('\u098D', '\u098E', state18), new Transition('\u058B', '\u0590', state18),
				new Transition('\uA4C1', '\uA4C1', state18), new Transition('\u2728', '\u2728', state18),
				new Transition('\u0656', '\u065F', state18), new Transition('\u1FF5', '\u1FF5', state18),
				new Transition('\u318F', '\u318F', state18), new Transition('\u0B29', '\u0B29', state18),
				new Transition('\u20B0', '\u20CF', state18), new Transition('\u0A5D', '\u0A5D', state18),
				new Transition('\u17EA', '\u17FF', state18), new Transition('\uFD90', '\uFD91', state18),
				new Transition('\u0991', '\u0992', state18), new Transition('\u0CC5', '\u0CC5', state18),
				new Transition('\u125E', '\u125F', state18), new Transition('\uA4C5', '\uA4C5', state18),
				new Transition('\u04C5', '\u04C6', state18), new Transition('\u0BF3', '\u0C00', state18),
				new Transition('\uFFF9', '\uFFFB', state2), new Transition('\u072D', '\u072F', state18),
				new Transition('\u03F6', '\u03FF', state18), new Transition('\u13F5', '\u1400', state18),
				new Transition('\u0A5F', '\u0A65', state18), new Transition('\u0CC9', '\u0CC9', state18),
				new Transition('\u04C9', '\u04CA', state18), new Transition('\u0B31', '\u0B31', state18),
				new Transition('\u2FFC', '\u2FFF', state18), new Transition('\u0D97', '\u0D99', state18),
				new Transition('\u10C6', '\u10CF', state18), new Transition('\uDFFF', '\uE000', state2),
				new Transition('\u33FF', '\u33FF', state18), new Transition('\u1FFF', '\u1FFF', state18),
				new Transition('\uDBFF', '\uDBFF', state5), new Transition('\uDBFF', '\uDBFF', state9));
		state1.alter(true, 0, new Transition('\uDC00', '\uDC00', state2));
		state2.alter(true, 0);
		state3.alter(false, 0, new Transition('\uDC01', '\uDC01', state2), new Transition('\uDC20', '\uDC7F', state2));
		state4.alter(false, 0, new Transition('\uDD73', '\uDD7A', state2));
		state5.alter(true, 0, new Transition('\uDFFD', '\uDFFD', state2));
		state6.alter(false, 0, new Transition('\uDC00', '\uDC00', state2));
		state7.alter(false, 0, new Transition('\uDFFD', '\uDFFD', state2));
		state8.alter(false, 2, new Transition('\uDC00', '\uDFFF', state18));
		state9.alter(false, 9, new Transition('\uDC00', '\uDFFC', state18),
				new Transition('\uDFFE', '\uDFFF', state18));
		state10.alter(false, 1, new Transition('\uDF24', '\uDF2F', state18),
				new Transition('\uDF4B', '\uDFFF', state18), new Transition('\uDC00', '\uDEFF', state18),
				new Transition('\uDF1F', '\uDF1F', state18));
		state11.alter(false, 8, new Transition('\uDE1E', '\uDFFF', state18));
		state12.alter(false, 11, new Transition('\uDCF6', '\uDCFF', state18),
				new Transition('\uDD27', '\uDD29', state18), new Transition('\uDDDE', '\uDFFF', state18));
		state13.alter(false, 5, new Transition('\uDCC1', '\uDCC1', state18),
				new Transition('\uDCA7', '\uDCA8', state18), new Transition('\uDCAD', '\uDCAD', state18),
				new Transition('\uDCBA', '\uDCBA', state18), new Transition('\uDD3A', '\uDD3A', state18),
				new Transition('\uDCA0', '\uDCA1', state18), new Transition('\uDCBC', '\uDCBC', state18),
				new Transition('\uDC55', '\uDC55', state18), new Transition('\uDD15', '\uDD15', state18),
				new Transition('\uDD47', '\uDD49', state18), new Transition('\uDCC4', '\uDCC4', state18),
				new Transition('\uDD51', '\uDD51', state18), new Transition('\uDC9D', '\uDC9D', state18),
				new Transition('\uDD1D', '\uDD1D', state18), new Transition('\uDCA3', '\uDCA4', state18),
				new Transition('\uDEA4', '\uDEA7', state18), new Transition('\uDD06', '\uDD06', state18),
				new Transition('\uDD45', '\uDD45', state18), new Transition('\uDD0B', '\uDD0C', state18),
				new Transition('\uDD3F', '\uDD3F', state18), new Transition('\uDFCA', '\uDFCD', state18));
		state14.alter(false, 6, new Transition('\uDC26', '\uDC27', state18),
				new Transition('\uDC4E', '\uDFFF', state18));
		state15.alter(false, 3, new Transition('\uDC00', '\uDED5', state18),
				new Transition('\uDED7', '\uDFFF', state18));
		state16.alter(false, 10, new Transition('\uDC01', '\uDFFF', state18));
		state17.alter(false, 7, new Transition('\uDC00', '\uDC00', state18),
				new Transition('\uDC02', '\uDC1F', state18), new Transition('\uDC80', '\uDFFF', state18));
		state18.alter(true, 4);
		return Automaton.load(state0, false, null);
	}

	private static Automaton getAut16() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3300', '\u33FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut17() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFE30', '\uFE4F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut18() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uF900', '\uFAFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut19() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDE1F', state2));
		state1.alter(false, 0, new Transition('\uD87E', '\uD87E', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut20() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2E80', '\u2EFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut21() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3000', '\u303F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut22() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u4E00', '\u9FFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut23() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3400', '\u4DB5', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut24() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		state0.alter(false, 0, new Transition('\uD869', '\uD869', state1), new Transition('\uD840', '\uD868', state4));
		state1.alter(false, 0, new Transition('\uDC00', '\uDED6', state2));
		state2.alter(true, 0);
		state3.alter(true, 0);
		state4.alter(false, 0, new Transition('\uDC00', '\uDFFF', state3));
		return Automaton.load(state0, false, null);
	}

	private static Automaton getAut25() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u007F', '\u009F', state1), new Transition('\u0000', '\u001F', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut26() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 2, new Transition('\uDD73', '\uDD7A', state3));
		state1.alter(false, 1, new Transition('\uDC01', '\uDC01', state3), new Transition('\uDC20', '\uDC7F', state3));
		state2.alter(false, 0, new Transition('\uD834', '\uD834', state0), new Transition('\u180B', '\u180E', state3),
				new Transition('\u206A', '\u206F', state3), new Transition('\uDB40', '\uDB40', state1),
				new Transition('\u200C', '\u200F', state3), new Transition('\uFFF9', '\uFFFB', state3),
				new Transition('\u070F', '\u070F', state3), new Transition('\u202A', '\u202E', state3),
				new Transition('\uFEFF', '\uFEFF', state3));
		state3.alter(true, 3);
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut27() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 1, new Transition('\uDC00', '\uDFFF', state2));
		state1.alter(false, 0, new Transition('\u0009', '\n', state2), new Transition('\r', '\r', state2),
				new Transition('\uE000', '\uFFFD', state2), new Transition('\uD800', '\uDBFF', state0),
				new Transition('\u0020', '\uD7FF', state2));
		state2.alter(true, 2);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut28() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u13A0', '\u13FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut29() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		state0.alter(false, 2, new Transition('\uDC00', '\uDFFF', state11));
		state1.alter(false, 9, new Transition('\uDC00', '\uDFFC', state11),
				new Transition('\uDFFE', '\uDFFF', state11));
		state2.alter(false, 1, new Transition('\uDF24', '\uDF2F', state11), new Transition('\uDF4B', '\uDFFF', state11),
				new Transition('\uDC00', '\uDEFF', state11), new Transition('\uDF1F', '\uDF1F', state11));
		state3.alter(false, 8, new Transition('\uDE1E', '\uDFFF', state11));
		state4.alter(false, 11, new Transition('\uDCF6', '\uDCFF', state11),
				new Transition('\uDD27', '\uDD29', state11), new Transition('\uDDDE', '\uDFFF', state11));
		state5.alter(false, 5, new Transition('\uDCC1', '\uDCC1', state11), new Transition('\uDCA7', '\uDCA8', state11),
				new Transition('\uDCAD', '\uDCAD', state11), new Transition('\uDCBA', '\uDCBA', state11),
				new Transition('\uDD3A', '\uDD3A', state11), new Transition('\uDCA0', '\uDCA1', state11),
				new Transition('\uDCBC', '\uDCBC', state11), new Transition('\uDC55', '\uDC55', state11),
				new Transition('\uDD15', '\uDD15', state11), new Transition('\uDD47', '\uDD49', state11),
				new Transition('\uDCC4', '\uDCC4', state11), new Transition('\uDD51', '\uDD51', state11),
				new Transition('\uDC9D', '\uDC9D', state11), new Transition('\uDD1D', '\uDD1D', state11),
				new Transition('\uDCA3', '\uDCA4', state11), new Transition('\uDEA4', '\uDEA7', state11),
				new Transition('\uDD06', '\uDD06', state11), new Transition('\uDD45', '\uDD45', state11),
				new Transition('\uDD0B', '\uDD0C', state11), new Transition('\uDD3F', '\uDD3F', state11),
				new Transition('\uDFCA', '\uDFCD', state11));
		state6.alter(false, 6, new Transition('\uDC26', '\uDC27', state11),
				new Transition('\uDC4E', '\uDFFF', state11));
		state7.alter(false, 3, new Transition('\uDC00', '\uDED5', state11),
				new Transition('\uDED7', '\uDFFF', state11));
		state8.alter(false, 10, new Transition('\uDC01', '\uDFFF', state11));
		state9.alter(false, 7, new Transition('\uDC00', '\uDC00', state11), new Transition('\uDC02', '\uDC1F', state11),
				new Transition('\uDC80', '\uDFFF', state11));
		state10.alter(false, 0, new Transition('\uD801', '\uD801', state6), new Transition('\uD800', '\uD800', state2),
				new Transition('\u32CC', '\u32CF', state11), new Transition('\u04CD', '\u04CF', state11),
				new Transition('\u0B34', '\u0B35', state11), new Transition('\u0B9B', '\u0B9B', state11),
				new Transition('\uFE67', '\uFE67', state11), new Transition('\uD869', '\uD869', state7),
				new Transition('\u0ACE', '\u0ACF', state11), new Transition('\u0ECE', '\u0ECF', state11),
				new Transition('\u2596', '\u259F', state11), new Transition('\u05F5', '\u060B', state11),
				new Transition('\u12CF', '\u12CF', state11), new Transition('\u0B9D', '\u0B9D', state11),
				new Transition('\u0A03', '\u0A04', state11), new Transition('\u0C04', '\u0C04', state11),
				new Transition('\uFB37', '\uFB37', state11), new Transition('\u0CCE', '\u0CD4', state11),
				new Transition('\uFE6C', '\uFE6F', state11), new Transition('\u1207', '\u1207', state11),
				new Transition('\u093A', '\u093B', state11), new Transition('\u0B3A', '\u0B3B', state11),
				new Transition('\u0BA0', '\u0BA2', state11), new Transition('\u066E', '\u066F', state11),
				new Transition('\u03A2', '\u03A2', state11), new Transition('\u05A2', '\u05A2', state11),
				new Transition('\u0D3A', '\u0D3D', state11), new Transition('\u12D7', '\u12D7', state11),
				new Transition('\uFB3D', '\uFB3D', state11), new Transition('\uDB81', '\uDBBE', state0),
				new Transition('\u0E5C', '\u0E80', state11), new Transition('\u11A3', '\u11A7', state11),
				new Transition('\u2071', '\u2073', state11), new Transition('\uFE73', '\uFE73', state11),
				new Transition('\u0AD1', '\u0ADF', state11), new Transition('\u0BA5', '\u0BA7', state11),
				new Transition('\uFB3F', '\uFB3F', state11), new Transition('\u0A0B', '\u0A0E', state11),
				new Transition('\u0C0D', '\u0C0D', state11), new Transition('\uDB40', '\uDB40', state9),
				new Transition('\u0CD7', '\u0CDD', state11), new Transition('\u0EDA', '\u0EDB', state11),
				new Transition('\u180F', '\u180F', state11), new Transition('\u09A9', '\u09A9', state11),
				new Transition('\uFE75', '\uFE75', state11), new Transition('\uFB42', '\uFB42', state11),
				new Transition('\uD86A', '\uD87D', state0), new Transition('\uFDFC', '\uFE1F', state11),
				new Transition('\u0C11', '\u0C11', state11), new Transition('\u0D44', '\u0D45', state11),
				new Transition('\u0A11', '\u0A12', state11), new Transition('\u0B44', '\u0B46', state11),
				new Transition('\u0CDF', '\u0CDF', state11), new Transition('\u0BAB', '\u0BAD', state11),
				new Transition('\uFB45', '\uFB45', state11), new Transition('\u16F1', '\u177F', state11),
				new Transition('\u1F46', '\u1F47', state11), new Transition('\u07B1', '\u0900', state11),
				new Transition('\u0C70', '\u0C81', state11), new Transition('\u1347', '\u1347', state11),
				new Transition('\u060D', '\u061A', state11), new Transition('\u0F48', '\u0F48', state11),
				new Transition('\uFD40', '\uFD4F', state11), new Transition('\u0A75', '\u0A80', state11),
				new Transition('\u1677', '\u167F', state11), new Transition('\u213B', '\u2152', state11),
				new Transition('\u0D49', '\u0D49', state11), new Transition('\u1878', '\u187F', state11),
				new Transition('\u0B49', '\u0B4A', state11), new Transition('\u2614', '\u2618', state11),
				new Transition('\u27B0', '\u27B0', state11), new Transition('\u0AE1', '\u0AE5', state11),
				new Transition('\u327C', '\u327E', state11), new Transition('\uD87E', '\uD87E', state3),
				new Transition('\u0CE2', '\u0CE5', state11), new Transition('\u09B1', '\u09B1', state11),
				new Transition('\u0DB2', '\u0DB2', state11), new Transition('\u274C', '\u274C', state11),
				new Transition('\u09B3', '\u09B5', state11), new Transition('\u274E', '\u274E', state11),
				new Transition('\u094E', '\u094F', state11), new Transition('\u1F4E', '\u1F4F', state11),
				new Transition('\u1FB5', '\u1FB5', state11), new Transition('\u0BB6', '\u0BB6', state11),
				new Transition('\u0E83', '\u0E83', state11), new Transition('\u105A', '\u109F', state11),
				new Transition('\u181A', '\u181F', state11), new Transition('\u061C', '\u061E', state11),
				new Transition('\u0A84', '\u0A84', state11), new Transition('\u0C84', '\u0C84', state11),
				new Transition('\u321D', '\u321F', state11), new Transition('\uD802', '\uD833', state0),
				new Transition('\u0B4E', '\u0B55', state11), new Transition('\u0E85', '\u0E86', state11),
				new Transition('\u0D4E', '\u0D56', state11), new Transition('\u0620', '\u0620', state11),
				new Transition('\u05BA', '\u05BA', state11), new Transition('\u0220', '\u0221', state11),
				new Transition('\u0487', '\u0487', state11), new Transition('\u1287', '\u1287', state11),
				new Transition('\u09BA', '\u09BB', state11), new Transition('\u2753', '\u2755', state11),
				new Transition('\u06EE', '\u06EF', state11), new Transition('\u1022', '\u1022', state11),
				new Transition('\u0BBA', '\u0BBD', state11), new Transition('\u12EF', '\u12EF', state11),
				new Transition('\u0DBC', '\u0DBC', state11), new Transition('\u0E89', '\u0E89', state11),
				new Transition('\u1289', '\u1289', state11), new Transition('\u0955', '\u0957', state11),
				new Transition('\u09BD', '\u09BD', state11), new Transition('\u0FBD', '\u0FBD', state11),
				new Transition('\u2757', '\u2757', state11), new Transition('\u048A', '\u048B', state11),
				new Transition('\u0557', '\u0558', state11), new Transition('\u1F58', '\u1F58', state11),
				new Transition('\u0DBE', '\u0DBF', state11), new Transition('\u0EDE', '\u0EFF', state11),
				new Transition('\u0E8B', '\u0E8C', state11), new Transition('\u034F', '\u035F', state11),
				new Transition('\u0A8C', '\u0A8C', state11), new Transition('\uD87F', '\uDB3F', state0),
				new Transition('\uDBBF', '\uDBBF', state1), new Transition('\u0B58', '\u0B5B', state11),
				new Transition('\u0C8D', '\u0C8D', state11), new Transition('\u1F5A', '\u1F5A', state11),
				new Transition('\uA48D', '\uA48F', state11), new Transition('\uDBC0', '\uDBC0', state8),
				new Transition('\u20E4', '\u20FF', state11), new Transition('\uFFBF', '\uFFC1', state11),
				new Transition('\u0A8E', '\u0A8E', state11), new Transition('\u1028', '\u1028', state11),
				new Transition('\u128E', '\u128F', state11), new Transition('\u1F5C', '\u1F5C', state11),
				new Transition('\u0A29', '\u0A29', state11), new Transition('\u0C29', '\u0C29', state11),
				new Transition('\u0D58', '\u0D5F', state11), new Transition('\u115A', '\u115E', state11),
				new Transition('\u04F6', '\u04F7', state11), new Transition('\uFE24', '\uFE2F', state11),
				new Transition('\u24EB', '\u24FF', state11), new Transition('\u0BC3', '\u0BC5', state11),
				new Transition('\u0C91', '\u0C91', state11), new Transition('\u0E8E', '\u0E93', state11),
				new Transition('\u0B5E', '\u0B5E', state11), new Transition('\u135B', '\u1360', state11),
				new Transition('\u1F5E', '\u1F5E', state11), new Transition('\u102B', '\u102B', state11),
				new Transition('\u1FC5', '\u1FC5', state11), new Transition('\u0A92', '\u0A92', state11),
				new Transition('\uFF5F', '\uFF60', state11), new Transition('\u02EF', '\u02FF', state11),
				new Transition('\u09C5', '\u09C6', state11), new Transition('\u10F7', '\u10FA', state11),
				new Transition('\u275F', '\u2760', state11), new Transition('\uFBB2', '\uFBD2', state11),
				new Transition('\u0560', '\u0560', state11), new Transition('\u0AF0', '\u0B00', state11),
				new Transition('\u22F2', '\u22FF', state11), new Transition('\u0CF0', '\u0D01', state11),
				new Transition('\u2EF4', '\u2EFF', state11), new Transition('\u0DC7', '\u0DC9', state11),
				new Transition('\uAC01', '\uD7A2', state11), new Transition('\u4E01', '\u9FA4', state11),
				new Transition('\u0BC9', '\u0BC9', state11), new Transition('\uFFC8', '\uFFC9', state11),
				new Transition('\u09C9', '\u09CA', state11), new Transition('\uFEFD', '\uFEFE', state11),
				new Transition('\u1EFA', '\u1EFF', state11), new Transition('\u3095', '\u3098', state11),
				new Transition('\u0B62', '\u0B65', state11), new Transition('\u0D62', '\u0D65', state11),
				new Transition('\u0A31', '\u0A31', state11), new Transition('\u10FC', '\u10FF', state11),
				new Transition('\u05C5', '\u05CF', state11), new Transition('\u0E98', '\u0E98', state11),
				new Transition('\uE001', '\uF8FE', state11), new Transition('\uDB41', '\uDB7F', state0),
				new Transition('\u06FF', '\u06FF', state11), new Transition('\u208F', '\u209F', state11),
				new Transition('\u32FF', '\u32FF', state11), new Transition('\u0DCB', '\u0DCE', state11),
				new Transition('\uD834', '\uD834', state4), new Transition('\u2E9A', '\u2E9A', state11),
				new Transition('\u0A34', '\u0A34', state11), new Transition('\u0C34', '\u0C34', state11),
				new Transition('\u0FCD', '\u0FCE', state11), new Transition('\uFF00', '\uFF00', state11),
				new Transition('\u1033', '\u1035', state11), new Transition('\u30FF', '\u3104', state11),
				new Transition('\u03CF', '\u03CF', state11), new Transition('\u2427', '\u243F', state11),
				new Transition('\uD835', '\uD835', state5), new Transition('\u074B', '\u077F', state11),
				new Transition('\u0A37', '\u0A37', state11), new Transition('\u0904', '\u0904', state11),
				new Transition('\u0B04', '\u0B04', state11), new Transition('\u0D04', '\u0D04', state11),
				new Transition('\u1E9C', '\u1E9F', state11), new Transition('\u169D', '\u169F', state11),
				new Transition('\uFFD0', '\uFFD1', state11), new Transition('\u2705', '\u2705', state11),
				new Transition('\u09CE', '\u09D6', state11), new Transition('\u0BCE', '\u0BD6', state11),
				new Transition('\u309F', '\u30A0', state11), new Transition('\u0363', '\u0373', state11),
				new Transition('\u3401', '\u4DB4', state11), new Transition('\u0EA0', '\u0EA0', state11),
				new Transition('\u0A3A', '\u0A3B', state11), new Transition('\u0F6B', '\u0F70', state11),
				new Transition('\u1FD4', '\u1FD5', state11), new Transition('\u0DD5', '\u0DD5', state11),
				new Transition('\u0C3A', '\u0C3D', state11), new Transition('\uD836', '\uD83F', state0),
				new Transition('\u303B', '\u303D', state11), new Transition('\uA4A2', '\uA4A3', state11),
				new Transition('\u2768', '\u2775', state11), new Transition('\u0E3B', '\u0E3E', state11),
				new Transition('\u0A3D', '\u0A3D', state11), new Transition('\u103A', '\u103F', state11),
				new Transition('\u063B', '\u063F', state11), new Transition('\u0DD7', '\u0DD7', state11),
				new Transition('\u239B', '\u23FF', state11), new Transition('\u0EA4', '\u0EA4', state11),
				new Transition('\u270A', '\u270B', state11), new Transition('\u03D8', '\u03D9', state11),
				new Transition('\u0EA6', '\u0EA6', state11), new Transition('\uFFD8', '\uFFD9', state11),
				new Transition('\u3040', '\u3040', state11), new Transition('\uFB07', '\uFB12', state11),
				new Transition('\u09D8', '\u09DB', state11), new Transition('\u0D0D', '\u0D0D', state11),
				new Transition('\uD7A4', '\uD7FF', state11), new Transition('\u0B0D', '\u0B0E', state11),
				new Transition('\uD840', '\uD840', state8), new Transition('\u070E', '\u070E', state11),
				new Transition('\u9FA6', '\u9FFF', state11), new Transition('\u0EA8', '\u0EA9', state11),
				new Transition('\u130F', '\u130F', state11), new Transition('\u1FDC', '\u1FDC', state11),
				new Transition('\u0AA9', '\u0AA9', state11), new Transition('\u0CA9', '\u0CA9', state11),
				new Transition('\u18AA', '\u1DFF', state11), new Transition('\uFFDD', '\uFFDF', state11),
				new Transition('\u0234', '\u024F', state11), new Transition('\u0D11', '\u0D11', state11),
				new Transition('\u1311', '\u1311', state11), new Transition('\u09DE', '\u09DE', state11),
				new Transition('\u0376', '\u0379', state11), new Transition('\u17DD', '\u17DF', state11),
				new Transition('\u0A43', '\u0A46', state11), new Transition('\u0B11', '\u0B12', state11),
				new Transition('\u33DE', '\u33DF', state11), new Transition('\u0C45', '\u0C45', state11),
				new Transition('\uFDC8', '\uFDEF', state11), new Transition('\u0EAC', '\u0EAC', state11),
				new Transition('\u3377', '\u337A', state11), new Transition('\uFA2E', '\uFAFF', state11),
				new Transition('\u0971', '\u0980', state11), new Transition('\u0BD8', '\u0BE6', state11),
				new Transition('\u0D70', '\u0D81', state11), new Transition('\u1247', '\u1247', state11),
				new Transition('\u2047', '\u2047', state11), new Transition('\u0B71', '\u0B81', state11),
				new Transition('\uFE45', '\uFE48', state11), new Transition('\u4DB6', '\u4DFF', state11),
				new Transition('\u02AE', '\u02AF', state11), new Transition('\u12AF', '\u12AF', state11),
				new Transition('\u237C', '\u237C', state11), new Transition('\u037B', '\u037D', state11),
				new Transition('\u0C49', '\u0C49', state11), new Transition('\u1249', '\u1249', state11),
				new Transition('\u31B8', '\u31FF', state11), new Transition('\u0A49', '\u0A4A', state11),
				new Transition('\u1316', '\u1317', state11), new Transition('\u1F16', '\u1F17', state11),
				new Transition('\u0AB1', '\u0AB1', state11), new Transition('\u12B1', '\u12B1', state11),
				new Transition('\u09E4', '\u09E5', state11), new Transition('\uDBC1', '\uDBFE', state0),
				new Transition('\u1F7E', '\u1F7F', state11), new Transition('\u2FD6', '\u2FEF', state11),
				new Transition('\u27BF', '\u27FF', state11), new Transition('\u2900', '\u2E7F', state11),
				new Transition('\uFB18', '\uFB1C', state11), new Transition('\u04FA', '\u0530', state11),
				new Transition('\u0AB4', '\u0AB4', state11), new Transition('\u0CB4', '\u0CB4', state11),
				new Transition('\uDB80', '\uDB80', state8), new Transition('\u037F', '\u0383', state11),
				new Transition('\uA4B4', '\uA4B4', state11), new Transition('\uFFE7', '\uFFE7', state11),
				new Transition('\uA4C7', '\uABFF', state11), new Transition('\u124E', '\u124F', state11),
				new Transition('\u12B6', '\u12B7', state11), new Transition('\u0DE0', '\u0DF1', state11),
				new Transition('\u0984', '\u0984', state11), new Transition('\u0B84', '\u0B84', state11),
				new Transition('\u0D84', '\u0D84', state11), new Transition('\u0C4E', '\u0C54', state11),
				new Transition('\u1F1E', '\u1F1F', state11), new Transition('\u131F', '\u131F', state11),
				new Transition('\uFE53', '\uFE53', state11), new Transition('\u0FD0', '\u0FFF', state11),
				new Transition('\u32B1', '\u32BF', state11), new Transition('\u0EBA', '\u0EBA', state11),
				new Transition('\u05EB', '\u05EF', state11), new Transition('\u0A4E', '\u0A58', state11),
				new Transition('\u0ABA', '\u0ABB', state11), new Transition('\u3244', '\u325F', state11),
				new Transition('\u0588', '\u0588', state11), new Transition('\u0CBA', '\u0CBD', state11),
				new Transition('\u1257', '\u1257', state11), new Transition('\u1FF0', '\u1FF1', state11),
				new Transition('\u244B', '\u245F', state11), new Transition('\u2184', '\u218F', state11),
				new Transition('\u038B', '\u038B', state11), new Transition('\u0EBE', '\u0EBF', state11),
				new Transition('\u12BF', '\u12BF', state11), new Transition('\u0B8B', '\u0B8D', state11),
				new Transition('\u1259', '\u1259', state11), new Transition('\uD841', '\uD868', state0),
				new Transition('\u038D', '\u038D', state11), new Transition('\uFFEF', '\uFFF8', state11),
				new Transition('\u098D', '\u098E', state11), new Transition('\u0F8C', '\u0F8F', state11),
				new Transition('\u12C1', '\u12C1', state11), new Transition('\u058B', '\u0590', state11),
				new Transition('\uA4C1', '\uA4C1', state11), new Transition('\u2728', '\u2728', state11),
				new Transition('\u0656', '\u065F', state11), new Transition('\u1FF5', '\u1FF5', state11),
				new Transition('\u0C57', '\u0C5F', state11), new Transition('\u318F', '\u318F', state11),
				new Transition('\u0B29', '\u0B29', state11), new Transition('\u0D29', '\u0D29', state11),
				new Transition('\u20B0', '\u20CF', state11), new Transition('\u0A5D', '\u0A5D', state11),
				new Transition('\u17EA', '\u17FF', state11), new Transition('\u0B91', '\u0B91', state11),
				new Transition('\u137D', '\u139F', state11), new Transition('\u204E', '\u2069', state11),
				new Transition('\uFD90', '\uFD91', state11), new Transition('\u0991', '\u0992', state11),
				new Transition('\u0CC5', '\u0CC5', state11), new Transition('\u0EC5', '\u0EC5', state11),
				new Transition('\u125E', '\u125F', state11), new Transition('\uA4C5', '\uA4C5', state11),
				new Transition('\u04C5', '\u04C6', state11), new Transition('\u0AC6', '\u0AC6', state11),
				new Transition('\u12C6', '\u12C7', state11), new Transition('\u0EC7', '\u0EC7', state11),
				new Transition('\u2672', '\u2700', state11), new Transition('\u21F4', '\u21FF', state11),
				new Transition('\u0BF3', '\u0C00', state11), new Transition('\u072D', '\u072F', state11),
				new Transition('\u03F6', '\u03FF', state11), new Transition('\u0DF5', '\u0E00', state11),
				new Transition('\u13F5', '\u1400', state11), new Transition('\u312D', '\u3130', state11),
				new Transition('\u0A5F', '\u0A65', state11), new Transition('\u0CC9', '\u0CC9', state11),
				new Transition('\u25F8', '\u25FF', state11), new Transition('\u2795', '\u2797', state11),
				new Transition('\u04C9', '\u04CA', state11), new Transition('\u11FA', '\u11FF', state11),
				new Transition('\u0ACA', '\u0ACA', state11), new Transition('\u0C62', '\u0C65', state11),
				new Transition('\u0B96', '\u0B98', state11), new Transition('\u0B31', '\u0B31', state11),
				new Transition('\u2FFC', '\u2FFF', state11), new Transition('\u0F98', '\u0F98', state11),
				new Transition('\u09FB', '\u0A01', state11), new Transition('\u0D97', '\u0D99', state11),
				new Transition('\u10C6', '\u10CF', state11), new Transition('\u33FF', '\u33FF', state11),
				new Transition('\u1FFF', '\u1FFF', state11), new Transition('\uDBFF', '\uDBFF', state1));
		state11.alter(true, 4);
		return Automaton.load(state10, true, null);
	}

	private static Automaton getAut30() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 0, new Transition('\uE000', '\uE000', state3), new Transition('\uDB80', '\uDB80', state2),
				new Transition('\uDBC0', '\uDBC0', state2), new Transition('\uF8FF', '\uF8FF', state3),
				new Transition('\uDBBF', '\uDBBF', state1), new Transition('\uDBFF', '\uDBFF', state1));
		state1.alter(false, 2, new Transition('\uDFFD', '\uDFFD', state3));
		state2.alter(false, 3, new Transition('\uDC00', '\uDC00', state3));
		state3.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut31() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0300', '\u036F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut32() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFE20', '\uFE2F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut33() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u20D0', '\u20FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut34() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2400', '\u243F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut35() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\uD800', '\uD800', state1), new Transition('\uDB7F', '\uDB80', state1),
				new Transition('\uDBFF', '\uDC00', state1), new Transition('\uDFFF', '\uDFFF', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut36() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u20A0', '\u20CF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut37() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0400', '\u04FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut38() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDC4F', state2));
		state1.alter(false, 0, new Transition('\uD801', '\uD801', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut39() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0900', '\u097F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut40() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2700', '\u27BF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut41() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2460', '\u24FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut42() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3200', '\u32FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut43() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1200', '\u137F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut44() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2000', '\u206F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut45() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u25A0', '\u25FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut46() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u10A0', '\u10FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut47() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDF30', '\uDF4F', state2));
		state1.alter(false, 0, new Transition('\uD800', '\uD800', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut48() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0370', '\u03FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut49() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1F00', '\u1FFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut50() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0A80', '\u0AFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut51() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0A00', '\u0A7F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut52() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFF00', '\uFFEF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut53() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3130', '\u318F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut54() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1100', '\u11FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut55() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uAC00', '\uD7A3', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut56() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0590', '\u05FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut57() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3040', '\u309F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut58() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0250', '\u02AF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut59() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2FF0', '\u2FFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut60() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u3190', '\u319F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut61() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2F00', '\u2FDF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut62() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0C80', '\u0CFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut63() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u30A0', '\u30FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut64() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1780', '\u17FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut65() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		state0.alter(false, 2, new Transition('\uDC00', '\uDC00', state7));
		state1.alter(false, 5, new Transition('\uDF00', '\uDF1E', state7), 
				new Transition('\uDF30', '\uDF49', state7));
		state2.alter(false, 3, new Transition('\uDD4A', '\uDD50', state7), 
				new Transition('\uDCAE', '\uDCB9', state7),
				new Transition('\uDC56', '\uDC9C', state7), new Transition('\uDD16', '\uDD1C', state7),
				new Transition('\uDD40', '\uDD44', state7), new Transition('\uDF8A', '\uDFA8', state7),
				new Transition('\uDCC2', '\uDCC3', state7), new Transition('\uDD52', '\uDEA3', state7),
				new Transition('\uDF16', '\uDF34', state7), new Transition('\uDCA9', '\uDCAC', state7),
				new Transition('\uDD0D', '\uDD14', state7), new Transition('\uDF36', '\uDF4E', state7),
				new Transition('\uDEA8', '\uDEC0', state7), new Transition('\uDEC2', '\uDEDA', state7),
				new Transition('\uDD46', '\uDD46', state7), new Transition('\uDC9E', '\uDC9F', state7),
				new Transition('\uDCC5', '\uDD05', state7), new Transition('\uDFAA', '\uDFC2', state7),
				new Transition('\uDEDC', '\uDEFA', state7), new Transition('\uDCBB', '\uDCBB', state7),
				new Transition('\uDD1E', '\uDD39', state7), new Transition('\uDFC4', '\uDFC9', state7),
				new Transition('\uDD07', '\uDD0A', state7), new Transition('\uDCA2', '\uDCA2', state7),
				new Transition('\uDF50', '\uDF6E', state7), new Transition('\uDEFC', '\uDF14', state7),
				new Transition('\uDD3B', '\uDD3E', state7), new Transition('\uDC00', '\uDC54', state7),
				new Transition('\uDCA5', '\uDCA6', state7), new Transition('\uDF70', '\uDF88', state7),
				new Transition('\uDCBD', '\uDCC0', state7));
		state3.alter(false, 0, new Transition('\uDC00', '\uDE1D', state7));
		state4.alter(false, 6, new Transition('\uDED6', '\uDED6', state7));
		state5.alter(false, 1, new Transition('\u0B99', '\u0B9A', state7), 
				new Transition('\uD801', '\uD801', state6),
				new Transition('\u3400', '\u3400', state7), new Transition('\u4E00', '\u4E00', state7),
				new Transition('\u00C0', '\u00D6', state7), new Transition('\uFF66', '\uFFBE', state7),
				new Transition('\uAC00', '\uAC00', state7), new Transition('\uD800', '\uD800', state1),
				new Transition('\uD869', '\uD869', state4), new Transition('\u1100', '\u1159', state7),
				new Transition('\u0B9C', '\u0B9C', state7), new Transition('\u1F20', '\u1F45', state7),
				new Transition('\u0AD0', '\u0AD0', state7), new Transition('\u2133', '\u2139', state7),
				new Transition('\u1200', '\u1206', state7), new Transition('\u1320', '\u1346', state7),
				new Transition('\u02D0', '\u02D1', state7), new Transition('\u0B36', '\u0B39', state7),
				new Transition('\u0B9E', '\u0B9F', state7), new Transition('\u1780', '\u17B3', state7),
				new Transition('\u1F80', '\u1FB4', state7), new Transition('\u3005', '\u3006', state7),
				new Transition('\u0993', '\u09A8', state7), new Transition('\uFB38', '\uFB3C', state7),
				new Transition('\u12D0', '\u12D6', state7), new Transition('\u0A05', '\u0A0A', state7),
				new Transition('\u06D5', '\u06D5', state7), new Transition('\u1EA0', '\u1EF9', state7),
				new Transition('\uD7A3', '\uD7A3', state7), new Transition('\u0C05', '\u0C0C', state7),
				new Transition('\u0061', '\u007A', state7), new Transition('\u30A1', '\u30FA', state7),
				new Transition('\u093D', '\u093D', state7), new Transition('\u0B3D', '\u0B3D', state7),
				new Transition('\u0BA3', '\u0BA4', state7), new Transition('\uFB3E', '\uFB3E', state7),
				new Transition('\uFE70', '\uFE72', state7), new Transition('\u9FA5', '\u9FA5', state7),
				new Transition('\u3041', '\u3094', state7), new Transition('\u0A72', '\u0A74', state7),
				new Transition('\u166F', '\u1676', state7), new Transition('\uFE74', '\uFE74', state7),
				new Transition('\u0D9A', '\u0DB1', state7), new Transition('\uFB40', '\uFB41', state7),
				new Transition('\u0C0E', '\u0C10', state7), new Transition('\u00F8', '\u021F', state7),
				new Transition('\u0A0F', '\u0A10', state7), new Transition('\u0BA8', '\u0BAA', state7),
				new Transition('\u0EDC', '\u0EDD', state7), new Transition('\u1260', '\u1286', state7),
				new Transition('\u0F40', '\u0F47', state7), new Transition('\u0CDE', '\u0CDE', state7),
				new Transition('\uFB43', '\uFB44', state7), new Transition('\u0AE0', '\u0AE0', state7),
				new Transition('\u0CE0', '\u0CE1', state7), new Transition('\u1000', '\u1021', state7),
				new Transition('\u0531', '\u0556', state7), new Transition('\u09AA', '\u09B0', state7),
				new Transition('\u31A0', '\u31B7', state7), new Transition('\u02E0', '\u02E4', state7),
				new Transition('\uD87E', '\uD87E', state3), new Transition('\u1F48', '\u1F4D', state7),
				new Transition('\u09B2', '\u09B2', state7), new Transition('\u12D8', '\u12EE', state7),
				new Transition('\u0BAE', '\u0BB5', state7), new Transition('\u207F', '\u207F', state7),
				new Transition('\u06E5', '\u06E6', state7), new Transition('\uFD92', '\uFDC7', state7),
				new Transition('\u04D0', '\u04F5', state7), new Transition('\u10D0', '\u10F6', state7),
				new Transition('\uF900', '\uFA2D', state7), new Transition('\u0E81', '\u0E82', state7),
				new Transition('\u4DB5', '\u4DB5', state7), new Transition('\u0950', '\u0950', state7),
				new Transition('\u00D8', '\u00F6', state7), new Transition('\u0E01', '\u0E30', state7),
				new Transition('\u0E84', '\u0E84', state7), new Transition('\uFF41', '\uFF5A', state7),
				new Transition('\u09B6', '\u09B9', state7), new Transition('\u0DB3', '\u0DBB', state7),
				new Transition('\u0BB7', '\u0BB9', state7), new Transition('\u0C12', '\u0C28', state7),
				new Transition('\u0A13', '\u0A28', state7), new Transition('\u1348', '\u135A', state7),
				new Transition('\u1FB6', '\u1FBC', state7), new Transition('\u1F50', '\u1F57', state7),
				new Transition('\u02EE', '\u02EE', state7), new Transition('\u0E87', '\u0E88', state7),
				new Transition('\u0250', '\u02AD', state7), new Transition('\u1288', '\u1288', state7),
				new Transition('\u0A85', '\u0A8B', state7), new Transition('\u0C85', '\u0C8C', state7),
				new Transition('\u03A3', '\u03CE', state7), new Transition('\u0DBD', '\u0DBD', state7),
				new Transition('\u0E8A', '\u0E8A', state7), new Transition('\u1FBE', '\u1FBE', state7),
				new Transition('\u1023', '\u1027', state7), new Transition('\u128A', '\u128D', state7),
				new Transition('\u0559', '\u0559', state7), new Transition('\u1F59', '\u1F59', state7),
				new Transition('\u0A8D', '\u0A8D', state7), new Transition('\u0E8D', '\u0E8D', state7),
				new Transition('\u1F5B', '\u1F5B', state7), new Transition('\u0C8E', '\u0C90', state7),
				new Transition('\u0B5C', '\u0B5D', state7), new Transition('\u0F49', '\u0F6A', state7),
				new Transition('\u1029', '\u102A', state7), new Transition('\u1681', '\u169A', state7),
				new Transition('\u1FC2', '\u1FC4', state7), new Transition('\u0A8F', '\u0A91', state7),
				new Transition('\u1F5D', '\u1F5D', state7), new Transition('\u0DC0', '\u0DC6', state7),
				new Transition('\u0958', '\u0961', state7), new Transition('\u04F8', '\u04F9', state7),
				new Transition('\u0222', '\u0233', state7), new Transition('\uFFC2', '\uFFC7', state7),
				new Transition('\u0B5F', '\u0B61', state7), new Transition('\u1208', '\u1246', state7),
				new Transition('\u0D60', '\u0D61', state7), new Transition('\u0A2A', '\u0A30', state7),
				new Transition('\u06FA', '\u06FC', state7), new Transition('\u0C2A', '\u0C33', state7),
				new Transition('\u0E94', '\u0E97', state7), new Transition('\u0621', '\u063A', state7),
				new Transition('\u1FC6', '\u1FCC', state7), new Transition('\u30FC', '\u30FE', state7),
				new Transition('\u1880', '\u18A8', state7), new Transition('\u0A32', '\u0A33', state7),
				new Transition('\u0E32', '\u0E33', state7), new Transition('\u0F00', '\u0F00', state7),
				new Transition('\u3031', '\u3035', state7), new Transition('\uFFCA', '\uFFCF', state7),
				new Transition('\u12F0', '\u130E', state7), new Transition('\u2102', '\u2102', state7),
				new Transition('\u0A35', '\u0A36', state7), new Transition('\u3131', '\u318E', state7),
				new Transition('\uD835', '\uD835', state2), new Transition('\u0E99', '\u0E9F', state7),
				new Transition('\u309D', '\u309E', state7), new Transition('\u0C35', '\u0C39', state7),
				new Transition('\uFB00', '\uFB06', state7), new Transition('\u1FD0', '\u1FD3', state7),
				new Transition('\u0A38', '\u0A39', state7), new Transition('\u0C92', '\u0CA8', state7),
				new Transition('\u13A0', '\u13F4', state7), new Transition('\u0A93', '\u0AA8', state7),
				new Transition('\u2107', '\u2107', state7), new Transition('\u03D0', '\u03D7', state7),
				new Transition('\u1290', '\u12AE', state7), new Transition('\u0EA1', '\u0EA3', state7),
				new Transition('\uFFD2', '\uFFD7', state7), new Transition('\u0B05', '\u0B0C', state7),
				new Transition('\u0D05', '\u0D0C', state7), new Transition('\u1F5F', '\u1F7D', state7),
				new Transition('\u0EA5', '\u0EA5', state7), new Transition('\u11A8', '\u11F9', state7),
				new Transition('\u1FD6', '\u1FDB', state7), new Transition('\u1F00', '\u1F15', state7),
				new Transition('\u0EA7', '\u0EA7', state7), new Transition('\uD840', '\uD840', state0),
				new Transition('\u1401', '\u166C', state7), new Transition('\uFD50', '\uFD8F', state7),
				new Transition('\u0D0E', '\u0D10', state7), new Transition('\uFFDA', '\uFFDC', state7),
				new Transition('\u210A', '\u2113', state7), new Transition('\u0B0F', '\u0B10', state7),
				new Transition('\u09DC', '\u09DD', state7), new Transition('\u0710', '\u0710', state7),
				new Transition('\u1310', '\u1310', state7), new Transition('\u00AA', '\u00AA', state7),
				new Transition('\u0E40', '\u0E46', state7), new Transition('\u0EAA', '\u0EAB', state7),
				new Transition('\u0561', '\u0587', state7), new Transition('\uFBD3', '\uFD3D', state7),
				new Transition('\u0671', '\u06D3', state7), new Transition('\u05D0', '\u05EA', state7),
				new Transition('\u0640', '\u064A', state7), new Transition('\u09DF', '\u09E1', state7),
				new Transition('\u037A', '\u037A', state7), new Transition('\u1312', '\u1315', state7),
				new Transition('\u048C', '\u04C4', state7), new Transition('\u0AAA', '\u0AB0', state7),
				new Transition('\u1248', '\u1248', state7), new Transition('\u2115', '\u2115', state7),
				new Transition('\u0EAD', '\u0EB0', state7), new Transition('\u0CAA', '\u0CB3', state7),
				new Transition('\uFB13', '\uFB17', state7), new Transition('\u12B0', '\u12B0', state7),
				new Transition('\u124A', '\u124D', state7), new Transition('\u0AB2', '\u0AB3', state7),
				new Transition('\u0EB2', '\u0EB3', state7), new Transition('\u0400', '\u0481', state7),
				new Transition('\u12B2', '\u12B5', state7), new Transition('\u1FE0', '\u1FEC', state7),
				new Transition('\u1F18', '\u1F1D', state7), new Transition('\u02B0', '\u02B8', state7),
				new Transition('\u00B5', '\u00B5', state7), new Transition('\u2119', '\u211D', state7),
				new Transition('\u1318', '\u131E', state7), new Transition('\u3105', '\u312C', state7),
				new Transition('\u10A0', '\u10C5', state7), new Transition('\u0041', '\u005A', state7),
				new Transition('\u03DA', '\u03F5', state7), new Transition('\u0AB5', '\u0AB9', state7),
				new Transition('\u0CB5', '\u0CB9', state7), new Transition('\uFB1D', '\uFB1D', state7),
				new Transition('\uFB46', '\uFBB1', state7), new Transition('\u0D12', '\u0D28', state7),
				new Transition('\u0386', '\u0386', state7), new Transition('\u0B13', '\u0B28', state7),
				new Transition('\u1050', '\u1055', state7), new Transition('\u00BA', '\u00BA', state7),
				new Transition('\u1250', '\u1256', state7), new Transition('\u115F', '\u11A2', state7),
				new Transition('\u1820', '\u1877', state7), new Transition('\uA000', '\uA48C', state7),
				new Transition('\u0712', '\u072C', state7), new Transition('\u0B85', '\u0B8A', state7),
				new Transition('\u12B8', '\u12BE', state7), new Transition('\u0388', '\u038A', state7),
				new Transition('\u0985', '\u098C', state7), new Transition('\u0ABD', '\u0ABD', state7),
				new Transition('\u0EBD', '\u0EBD', state7), new Transition('\u0F88', '\u0F8B', state7),
				new Transition('\uFB1F', '\uFB28', state7), new Transition('\u09F0', '\u09F1', state7),
				new Transition('\u2124', '\u2124', state7), new Transition('\u0905', '\u0939', state7),
				new Transition('\u05F0', '\u05F2', state7), new Transition('\u1258', '\u1258', state7),
				new Transition('\u02BB', '\u02C1', state7), new Transition('\u038C', '\u038C', state7),
				new Transition('\u2126', '\u2126', state7), new Transition('\u12C0', '\u12C0', state7),
				new Transition('\u1FF2', '\u1FF4', state7), new Transition('\u0A59', '\u0A5C', state7),
				new Transition('\u2128', '\u2128', state7), new Transition('\u125A', '\u125D', state7),
				new Transition('\u0B8E', '\u0B90', state7), new Transition('\u0D85', '\u0D96', state7),
				new Transition('\u0EC0', '\u0EC4', state7), new Transition('\u098F', '\u0990', state7),
				new Transition('\u1E00', '\u1E9B', state7), new Transition('\u12C2', '\u12C5', state7),
				new Transition('\uFDF0', '\uFDFB', state7), new Transition('\u0A5E', '\u0A5E', state7),
				new Transition('\u212A', '\u212D', state7), new Transition('\u0EC6', '\u0EC6', state7),
				new Transition('\u1FF6', '\u1FFC', state7), new Transition('\u0B92', '\u0B95', state7),
				new Transition('\u0C60', '\u0C61', state7), new Transition('\u0B2A', '\u0B30', state7),
				new Transition('\uFE76', '\uFEFC', state7), new Transition('\u04C7', '\u04C8', state7),
				new Transition('\u0780', '\u07A5', state7), new Transition('\u212F', '\u2131', state7),
				new Transition('\uFB2A', '\uFB36', state7), new Transition('\uFF21', '\uFF3A', state7),
				new Transition('\u04CB', '\u04CC', state7), new Transition('\u12C8', '\u12CE', state7),
				new Transition('\u0B32', '\u0B33', state7), new Transition('\u16A0', '\u16EA', state7),
				new Transition('\u038E', '\u03A1', state7), new Transition('\u0D2A', '\u0D39', state7));
		state6.alter(false, 4, new Transition('\uDC28', '\uDC4D', state7), new Transition('\uDC00', '\uDC25', state7));
		state7.alter(true, 7);
		return Automaton.load(state5, true, null);
	}

	private static Automaton getAut66() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0E80', '\u0EFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut67() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0080', '\u00FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut68() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0100', '\u017F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut69() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0180', '\u024F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut70() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1E00', '\u1EFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut71() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u093D', '\u093D', state1), new Transition('\u0B3D', '\u0B3D', state1),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u113E', '\u113E', state1),
				new Transition('\u01FA', '\u0217', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u0A72', '\u0A74', state1),
				new Transition('\u1140', '\u1140', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u11BA', '\u11BA', state1),
				new Transition('\u1F50', '\u1F57', state1), new Transition('\u0E87', '\u0E88', state1),
				new Transition('\u1154', '\u1155', state1), new Transition('\u0A85', '\u0A8B', state1),
				new Transition('\u0C85', '\u0C8C', state1), new Transition('\u03A3', '\u03CE', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u1FBE', '\u1FBE', state1),
				new Transition('\u04EE', '\u04F5', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u3021', '\u3029', state1), new Transition('\u11BC', '\u11C2', state1),
				new Transition('\u0A8D', '\u0A8D', state1), new Transition('\u0E8D', '\u0E8D', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u0C8E', '\u0C90', state1),
				new Transition('\u0F49', '\u0F69', state1), new Transition('\u0B5C', '\u0B5D', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u0958', '\u0961', state1),
				new Transition('\u04F8', '\u04F9', state1), new Transition('\u0B5F', '\u0B61', state1),
				new Transition('\u115F', '\u1161', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u0A2A', '\u0A30', state1), new Transition('\uAC00', '\uD7A3', state1),
				new Transition('\u0C2A', '\u0C33', state1), new Transition('\u0E94', '\u0E97', state1),
				new Transition('\u4E00', '\u9FA5', state1), new Transition('\u1163', '\u1163', state1),
				new Transition('\u0621', '\u063A', state1), new Transition('\u0E30', '\u0E30', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u1165', '\u1165', state1),
				new Transition('\u0A32', '\u0A33', state1), new Transition('\u0E32', '\u0E33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0671', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u1102', '\u1103', state1),
				new Transition('\u1169', '\u1169', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E99', '\u0E9F', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u1FD0', '\u1FD3', state1), new Transition('\u0A38', '\u0A39', state1),
				new Transition('\u0C92', '\u0CA8', state1), new Transition('\u0A93', '\u0AA8', state1),
				new Transition('\u1105', '\u1107', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u0E40', '\u0E45', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0641', '\u064A', state1),
				new Transition('\u09DF', '\u09E1', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0EB0', '\u0EB0', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u0EB2', '\u0EB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0386', '\u0386', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0388', '\u038A', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0ABD', '\u0ABD', state1), new Transition('\u0EBD', '\u0EBD', state1),
				new Transition('\u09F0', '\u09F1', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u11F9', '\u11F9', state1), new Transition('\u1FF6', '\u1FFC', state1),
				new Transition('\u0B92', '\u0B95', state1), new Transition('\u0C60', '\u0C61', state1),
				new Transition('\u0B2A', '\u0B30', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u06C0', '\u06CE', state1),
				new Transition('\u04CB', '\u04CC', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut72() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2100', '\u214F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut73() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 3, new Transition('\uDF8A', '\uDF8F', state3), new Transition('\uDC1A', '\uDC33', state3),
				new Transition('\uDCC2', '\uDCC3', state3), new Transition('\uDC82', '\uDC9B', state3),
				new Transition('\uDF36', '\uDF4E', state3), new Transition('\uDCB6', '\uDCB9', state3),
				new Transition('\uDEC2', '\uDEDA', state3), new Transition('\uDCEA', '\uDD03', state3),
				new Transition('\uDF50', '\uDF55', state3), new Transition('\uDC4E', '\uDC54', state3),
				new Transition('\uDFAA', '\uDFC2', state3), new Transition('\uDEDC', '\uDEE1', state3),
				new Transition('\uDD52', '\uDD6B', state3), new Transition('\uDCBB', '\uDCBB', state3),
				new Transition('\uDC56', '\uDC67', state3), new Transition('\uDD1E', '\uDD37', state3),
				new Transition('\uDFC4', '\uDFC9', state3), new Transition('\uDDBA', '\uDDD3', state3),
				new Transition('\uDD86', '\uDD9F', state3), new Transition('\uDEFC', '\uDF14', state3),
				new Transition('\uDE22', '\uDE3B', state3), new Transition('\uDCC5', '\uDCCF', state3),
				new Transition('\uDDEE', '\uDE07', state3), new Transition('\uDE8A', '\uDEA3', state3),
				new Transition('\uDF16', '\uDF1B', state3), new Transition('\uDF70', '\uDF88', state3),
				new Transition('\uDE56', '\uDE6F', state3), new Transition('\uDCBD', '\uDCC0', state3));
		state1.alter(false, 1, new Transition('\u1ECD', '\u1ECD', state3), new Transition('\uD801', '\uD801', state2),
				new Transition('\u0199', '\u019B', state3), new Transition('\u0467', '\u0467', state3),
				new Transition('\u1E67', '\u1E67', state3), new Transition('\u2134', '\u2134', state3),
				new Transition('\u0201', '\u0201', state3), new Transition('\u1E01', '\u1E01', state3),
				new Transition('\u1F30', '\u1F37', state3), new Transition('\u0135', '\u0135', state3),
				new Transition('\u1ECF', '\u1ECF', state3), new Transition('\u0469', '\u0469', state3),
				new Transition('\u1E69', '\u1E69', state3), new Transition('\u0203', '\u0203', state3),
				new Transition('\u1E03', '\u1E03', state3), new Transition('\u04D1', '\u04D1', state3),
				new Transition('\u1ED1', '\u1ED1', state3), new Transition('\u0137', '\u0138', state3),
				new Transition('\u019E', '\u019E', state3), new Transition('\u046B', '\u046B', state3),
				new Transition('\u1E6B', '\u1E6B', state3), new Transition('\u0205', '\u0205', state3),
				new Transition('\u1E05', '\u1E05', state3), new Transition('\u2139', '\u2139', state3),
				new Transition('\u04D3', '\u04D3', state3), new Transition('\u1ED3', '\u1ED3', state3),
				new Transition('\u046D', '\u046D', state3), new Transition('\u1E6D', '\u1E6D', state3),
				new Transition('\u013A', '\u013A', state3), new Transition('\u0207', '\u0207', state3),
				new Transition('\u1E07', '\u1E07', state3), new Transition('\u01A1', '\u01A1', state3),
				new Transition('\u04D5', '\u04D5', state3), new Transition('\u1ED5', '\u1ED5', state3),
				new Transition('\u046F', '\u046F', state3), new Transition('\u1E6F', '\u1E6F', state3),
				new Transition('\u013C', '\u013C', state3), new Transition('\u0209', '\u0209', state3),
				new Transition('\u1E09', '\u1E09', state3), new Transition('\u01A3', '\u01A3', state3),
				new Transition('\u0061', '\u007A', state3), new Transition('\u04D7', '\u04D7', state3),
				new Transition('\u1ED7', '\u1ED7', state3), new Transition('\u0471', '\u0471', state3),
				new Transition('\u1E71', '\u1E71', state3), new Transition('\u1FA0', '\u1FA7', state3),
				new Transition('\u013E', '\u013E', state3), new Transition('\u020B', '\u020B', state3),
				new Transition('\u1E0B', '\u1E0B', state3), new Transition('\u01A5', '\u01A5', state3),
				new Transition('\u04D9', '\u04D9', state3), new Transition('\u1ED9', '\u1ED9', state3),
				new Transition('\u0473', '\u0473', state3), new Transition('\u1E73', '\u1E73', state3),
				new Transition('\u0140', '\u0140', state3), new Transition('\u020D', '\u020D', state3),
				new Transition('\u1E0D', '\u1E0D', state3), new Transition('\u04DB', '\u04DB', state3),
				new Transition('\u1EDB', '\u1EDB', state3), new Transition('\u01A8', '\u01A8', state3),
				new Transition('\u0475', '\u0475', state3), new Transition('\u1E75', '\u1E75', state3),
				new Transition('\u0142', '\u0142', state3), new Transition('\u020F', '\u020F', state3),
				new Transition('\u1E0F', '\u1E0F', state3), new Transition('\u1F40', '\u1F45', state3),
				new Transition('\u04DD', '\u04DD', state3), new Transition('\u1EDD', '\u1EDD', state3),
				new Transition('\u0477', '\u0477', state3), new Transition('\u1E77', '\u1E77', state3),
				new Transition('\u0144', '\u0144', state3), new Transition('\u01AA', '\u01AB', state3),
				new Transition('\u0211', '\u0211', state3), new Transition('\u1E11', '\u1E11', state3),
				new Transition('\u04DF', '\u04DF', state3), new Transition('\u1EDF', '\u1EDF', state3),
				new Transition('\u0479', '\u0479', state3), new Transition('\u1E79', '\u1E79', state3),
				new Transition('\u0146', '\u0146', state3), new Transition('\u0213', '\u0213', state3),
				new Transition('\u1E13', '\u1E13', state3), new Transition('\u01AD', '\u01AD', state3),
				new Transition('\u04E1', '\u04E1', state3), new Transition('\u1EE1', '\u1EE1', state3),
				new Transition('\u047B', '\u047B', state3), new Transition('\u1E7B', '\u1E7B', state3),
				new Transition('\u0215', '\u0215', state3), new Transition('\u1E15', '\u1E15', state3),
				new Transition('\u0148', '\u0149', state3), new Transition('\u04E3', '\u04E3', state3),
				new Transition('\u1EE3', '\u1EE3', state3), new Transition('\u01B0', '\u01B0', state3),
				new Transition('\u047D', '\u047D', state3), new Transition('\u1E7D', '\u1E7D', state3),
				new Transition('\u0217', '\u0217', state3), new Transition('\u1E17', '\u1E17', state3),
				new Transition('\u014B', '\u014B', state3), new Transition('\u04E5', '\u04E5', state3),
				new Transition('\u1EE5', '\u1EE5', state3), new Transition('\u047F', '\u047F', state3),
				new Transition('\u1E7F', '\u1E7F', state3), new Transition('\u207F', '\u207F', state3),
				new Transition('\u1FB0', '\u1FB4', state3), new Transition('\u0219', '\u0219', state3),
				new Transition('\u1E19', '\u1E19', state3), new Transition('\u014D', '\u014D', state3),
				new Transition('\u04E7', '\u04E7', state3), new Transition('\u1EE7', '\u1EE7', state3),
				new Transition('\u01B4', '\u01B4', state3), new Transition('\u0481', '\u0481', state3),
				new Transition('\u1E81', '\u1E81', state3), new Transition('\u021B', '\u021B', state3),
				new Transition('\u1E1B', '\u1E1B', state3), new Transition('\u014F', '\u014F', state3),
				new Transition('\u04E9', '\u04E9', state3), new Transition('\u1EE9', '\u1EE9', state3),
				new Transition('\u01B6', '\u01B6', state3), new Transition('\u1E83', '\u1E83', state3),
				new Transition('\u021D', '\u021D', state3), new Transition('\u1E1D', '\u1E1D', state3),
				new Transition('\u1FB6', '\u1FB7', state3), new Transition('\uFF41', '\uFF5A', state3),
				new Transition('\u0151', '\u0151', state3), new Transition('\u04EB', '\u04EB', state3),
				new Transition('\u1EEB', '\u1EEB', state3), new Transition('\u1E85', '\u1E85', state3),
				new Transition('\u021F', '\u021F', state3), new Transition('\u1E1F', '\u1E1F', state3),
				new Transition('\u0153', '\u0153', state3), new Transition('\u00DF', '\u00F6', state3),
				new Transition('\u01B9', '\u01BA', state3), new Transition('\u04ED', '\u04ED', state3),
				new Transition('\u1EED', '\u1EED', state3), new Transition('\u1E87', '\u1E87', state3),
				new Transition('\u1E21', '\u1E21', state3), new Transition('\u1F50', '\u1F57', state3),
				new Transition('\u0250', '\u02AD', state3), new Transition('\u0155', '\u0155', state3),
				new Transition('\u04EF', '\u04EF', state3), new Transition('\u1EEF', '\u1EEF', state3),
				new Transition('\u1E89', '\u1E89', state3), new Transition('\u0223', '\u0223', state3),
				new Transition('\u1E23', '\u1E23', state3), new Transition('\u0157', '\u0157', state3),
				new Transition('\u04F1', '\u04F1', state3), new Transition('\u1EF1', '\u1EF1', state3),
				new Transition('\u1FBE', '\u1FBE', state3), new Transition('\u01BD', '\u01BF', state3),
				new Transition('\u1E8B', '\u1E8B', state3), new Transition('\u0225', '\u0225', state3),
				new Transition('\u1E25', '\u1E25', state3), new Transition('\u0159', '\u0159', state3),
				new Transition('\u04F3', '\u04F3', state3), new Transition('\u1EF3', '\u1EF3', state3),
				new Transition('\u048D', '\u048D', state3), new Transition('\u1E8D', '\u1E8D', state3),
				new Transition('\u03AC', '\u03CE', state3), new Transition('\u0227', '\u0227', state3),
				new Transition('\u1E27', '\u1E27', state3), new Transition('\u015B', '\u015B', state3),
				new Transition('\u04F5', '\u04F5', state3), new Transition('\u1EF5', '\u1EF5', state3),
				new Transition('\u048F', '\u048F', state3), new Transition('\u1E8F', '\u1E8F', state3),
				new Transition('\u0229', '\u0229', state3), new Transition('\u1E29', '\u1E29', state3),
				new Transition('\u1FC2', '\u1FC4', state3), new Transition('\u015D', '\u015D', state3),
				new Transition('\u1EF7', '\u1EF7', state3), new Transition('\u0491', '\u0491', state3),
				new Transition('\u1E91', '\u1E91', state3), new Transition('\u022B', '\u022B', state3),
				new Transition('\u1E2B', '\u1E2B', state3), new Transition('\u015F', '\u015F', state3),
				new Transition('\u04F9', '\u04F9', state3), new Transition('\u1EF9', '\u1EF9', state3),
				new Transition('\u01C6', '\u01C6', state3), new Transition('\u0493', '\u0493', state3),
				new Transition('\u1E93', '\u1E93', state3), new Transition('\u022D', '\u022D', state3),
				new Transition('\u1E2D', '\u1E2D', state3), new Transition('\u1FC6', '\u1FC7', state3),
				new Transition('\u0161', '\u0161', state3), new Transition('\u0495', '\u0495', state3),
				new Transition('\u022F', '\u022F', state3), new Transition('\u1E2F', '\u1E2F', state3),
				new Transition('\u00F8', '\u00FF', state3), new Transition('\u01C9', '\u01C9', state3),
				new Transition('\u0163', '\u0163', state3), new Transition('\u0497', '\u0497', state3),
				new Transition('\u0231', '\u0231', state3), new Transition('\u1E31', '\u1E31', state3),
				new Transition('\u1F60', '\u1F67', state3), new Transition('\u0165', '\u0165', state3),
				new Transition('\u1E95', '\u1E9B', state3), new Transition('\u01CC', '\u01CC', state3),
				new Transition('\u0499', '\u0499', state3), new Transition('\u0233', '\u0233', state3),
				new Transition('\u1E33', '\u1E33', state3), new Transition('\u0167', '\u0167', state3),
				new Transition('\u0101', '\u0101', state3), new Transition('\u01CE', '\u01CE', state3),
				new Transition('\u049B', '\u049B', state3), new Transition('\u1E35', '\u1E35', state3),
				new Transition('\u0169', '\u0169', state3), new Transition('\uD835', '\uD835', state0),
				new Transition('\u0103', '\u0103', state3), new Transition('\u01D0', '\u01D0', state3),
				new Transition('\u049D', '\u049D', state3), new Transition('\u03D0', '\u03D1', state3),
				new Transition('\u1E37', '\u1E37', state3), new Transition('\u1F00', '\u1F07', state3),
				new Transition('\uFB00', '\uFB06', state3), new Transition('\u016B', '\u016B', state3),
				new Transition('\u0105', '\u0105', state3), new Transition('\u1FD0', '\u1FD3', state3),
				new Transition('\u01D2', '\u01D2', state3), new Transition('\u049F', '\u049F', state3),
				new Transition('\u1E39', '\u1E39', state3), new Transition('\u016D', '\u016D', state3),
				new Transition('\u0107', '\u0107', state3), new Transition('\u01D4', '\u01D4', state3),
				new Transition('\u04A1', '\u04A1', state3), new Transition('\u1EA1', '\u1EA1', state3),
				new Transition('\u1E3B', '\u1E3B', state3), new Transition('\u016F', '\u016F', state3),
				new Transition('\u0109', '\u0109', state3), new Transition('\u01D6', '\u01D6', state3),
				new Transition('\u03D5', '\u03D7', state3), new Transition('\u04A3', '\u04A3', state3),
				new Transition('\u1EA3', '\u1EA3', state3), new Transition('\u1E3D', '\u1E3D', state3),
				new Transition('\u1FD6', '\u1FD7', state3), new Transition('\u210A', '\u210A', state3),
				new Transition('\u0171', '\u0171', state3), new Transition('\u010B', '\u010B', state3),
				new Transition('\u01D8', '\u01D8', state3), new Transition('\u04A5', '\u04A5', state3),
				new Transition('\u1EA5', '\u1EA5', state3), new Transition('\u1E3F', '\u1E3F', state3),
				new Transition('\u0173', '\u0173', state3), new Transition('\u010D', '\u010D', state3),
				new Transition('\u01DA', '\u01DA', state3), new Transition('\u04A7', '\u04A7', state3),
				new Transition('\u1EA7', '\u1EA7', state3), new Transition('\u1E41', '\u1E41', state3),
				new Transition('\u03DB', '\u03DB', state3), new Transition('\u0175', '\u0175', state3),
				new Transition('\u210E', '\u210F', state3), new Transition('\u010F', '\u010F', state3),
				new Transition('\u04A9', '\u04A9', state3), new Transition('\u1EA9', '\u1EA9', state3),
				new Transition('\u01DC', '\u01DD', state3), new Transition('\u1E43', '\u1E43', state3),
				new Transition('\u03DD', '\u03DD', state3), new Transition('\u00AA', '\u00AA', state3),
				new Transition('\u0177', '\u0177', state3), new Transition('\u0111', '\u0111', state3),
				new Transition('\u04AB', '\u04AB', state3), new Transition('\u0561', '\u0587', state3),
				new Transition('\u1EAB', '\u1EAB', state3), new Transition('\u1F70', '\u1F7D', state3),
				new Transition('\u1E45', '\u1E45', state3), new Transition('\u01DF', '\u01DF', state3),
				new Transition('\u03DF', '\u03DF', state3), new Transition('\u0113', '\u0113', state3),
				new Transition('\u1F10', '\u1F15', state3), new Transition('\u2113', '\u2113', state3),
				new Transition('\u04AD', '\u04AD', state3), new Transition('\u1EAD', '\u1EAD', state3),
				new Transition('\u017A', '\u017A', state3), new Transition('\u1E47', '\u1E47', state3),
				new Transition('\u01E1', '\u01E1', state3), new Transition('\u03E1', '\u03E1', state3),
				new Transition('\u0115', '\u0115', state3), new Transition('\u04AF', '\u04AF', state3),
				new Transition('\u1EAF', '\u1EAF', state3), new Transition('\u017C', '\u017C', state3),
				new Transition('\u1E49', '\u1E49', state3), new Transition('\u01E3', '\u01E3', state3),
				new Transition('\u03E3', '\u03E3', state3), new Transition('\uFB13', '\uFB17', state3),
				new Transition('\u0117', '\u0117', state3), new Transition('\u04B1', '\u04B1', state3),
				new Transition('\u1EB1', '\u1EB1', state3), new Transition('\u1FE0', '\u1FE7', state3),
				new Transition('\u1E4B', '\u1E4B', state3), new Transition('\u01E5', '\u01E5', state3),
				new Transition('\u03E5', '\u03E5', state3), new Transition('\u017E', '\u0180', state3),
				new Transition('\u0119', '\u0119', state3), new Transition('\u0430', '\u045F', state3),
				new Transition('\u04B3', '\u04B3', state3), new Transition('\u1EB3', '\u1EB3', state3),
				new Transition('\u1E4D', '\u1E4D', state3), new Transition('\u01E7', '\u01E7', state3),
				new Transition('\u03E7', '\u03E7', state3), new Transition('\u011B', '\u011B', state3),
				new Transition('\u00B5', '\u00B5', state3), new Transition('\u04B5', '\u04B5', state3),
				new Transition('\u1EB5', '\u1EB5', state3), new Transition('\u1E4F', '\u1E4F', state3),
				new Transition('\u01E9', '\u01E9', state3), new Transition('\u03E9', '\u03E9', state3),
				new Transition('\u0183', '\u0183', state3), new Transition('\u011D', '\u011D', state3),
				new Transition('\u04B7', '\u04B7', state3), new Transition('\u1EB7', '\u1EB7', state3),
				new Transition('\u1E51', '\u1E51', state3), new Transition('\u1F80', '\u1F87', state3),
				new Transition('\u01EB', '\u01EB', state3), new Transition('\u03EB', '\u03EB', state3),
				new Transition('\u0185', '\u0185', state3), new Transition('\u011F', '\u011F', state3),
				new Transition('\u04B9', '\u04B9', state3), new Transition('\u1EB9', '\u1EB9', state3),
				new Transition('\u1E53', '\u1E53', state3), new Transition('\u01ED', '\u01ED', state3),
				new Transition('\u03ED', '\u03ED', state3), new Transition('\u00BA', '\u00BA', state3),
				new Transition('\u0121', '\u0121', state3), new Transition('\u04BB', '\u04BB', state3),
				new Transition('\u1EBB', '\u1EBB', state3), new Transition('\u0188', '\u0188', state3),
				new Transition('\u1E55', '\u1E55', state3), new Transition('\u01EF', '\u01F0', state3),
				new Transition('\u0123', '\u0123', state3), new Transition('\u04BD', '\u04BD', state3),
				new Transition('\u1EBD', '\u1EBD', state3), new Transition('\u1E57', '\u1E57', state3),
				new Transition('\u1F20', '\u1F27', state3), new Transition('\u03EF', '\u03F3', state3),
				new Transition('\u0125', '\u0125', state3), new Transition('\u04BF', '\u04BF', state3),
				new Transition('\u1EBF', '\u1EBF', state3), new Transition('\u1E59', '\u1E59', state3),
				new Transition('\u018C', '\u018D', state3), new Transition('\u01F3', '\u01F3', state3),
				new Transition('\u1FF2', '\u1FF4', state3), new Transition('\u0127', '\u0127', state3),
				new Transition('\u1EC1', '\u1EC1', state3), new Transition('\u1E5B', '\u1E5B', state3),
				new Transition('\u01F5', '\u01F5', state3), new Transition('\u03F5', '\u03F5', state3),
				new Transition('\u04C2', '\u04C2', state3), new Transition('\u0129', '\u0129', state3),
				new Transition('\u1EC3', '\u1EC3', state3), new Transition('\u0390', '\u0390', state3),
				new Transition('\u1E5D', '\u1E5D', state3), new Transition('\u1FF6', '\u1FF7', state3),
				new Transition('\u04C4', '\u04C4', state3), new Transition('\u012B', '\u012B', state3),
				new Transition('\u1EC5', '\u1EC5', state3), new Transition('\u0192', '\u0192', state3),
				new Transition('\u1E5F', '\u1E5F', state3), new Transition('\u01F9', '\u01F9', state3),
				new Transition('\u012D', '\u012D', state3), new Transition('\u1EC7', '\u1EC7', state3),
				new Transition('\u0461', '\u0461', state3), new Transition('\u1E61', '\u1E61', state3),
				new Transition('\u1F90', '\u1F97', state3), new Transition('\u01FB', '\u01FB', state3),
				new Transition('\u04C8', '\u04C8', state3), new Transition('\u0195', '\u0195', state3),
				new Transition('\u012F', '\u012F', state3), new Transition('\u212F', '\u212F', state3),
				new Transition('\u1EC9', '\u1EC9', state3), new Transition('\u0463', '\u0463', state3),
				new Transition('\u1E63', '\u1E63', state3), new Transition('\u01FD', '\u01FD', state3),
				new Transition('\u0131', '\u0131', state3), new Transition('\u1ECB', '\u1ECB', state3),
				new Transition('\u0465', '\u0465', state3), new Transition('\u1E65', '\u1E65', state3),
				new Transition('\u01FF', '\u01FF', state3), new Transition('\u04CC', '\u04CC', state3),
				new Transition('\u0133', '\u0133', state3));
		state2.alter(false, 2, new Transition('\uDC28', '\uDC4D', state3));
		state3.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut74() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u0640', '\u0640', state1), new Transition('\u3031', '\u3035', state1),
				new Transition('\u037A', '\u037A', state1), new Transition('\u02EE', '\u02EE', state1),
				new Transition('\u02B0', '\u02B8', state1), new Transition('\u02E0', '\u02E4', state1),
				new Transition('\u1843', '\u1843', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u309D', '\u309E', state1),
				new Transition('\uFF70', '\uFF70', state1), new Transition('\u02BB', '\u02C1', state1),
				new Transition('\u3005', '\u3005', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\uFF9E', '\uFF9F', state1),
				new Transition('\u0E46', '\u0E46', state1), new Transition('\u0EC6', '\u0EC6', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut75() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		state0.alter(false, 0, new Transition('\uDF00', '\uDF1E', state5), new Transition('\uDF30', '\uDF49', state5));
		state1.alter(false, 5, new Transition('\uDED6', '\uDED6', state5));
		state2.alter(false, 2, new Transition('\uDC00', '\uDE1D', state5));
		state3.alter(false, 3, new Transition('\uDC00', '\uDC00', state5));
		state4.alter(false, 4, new Transition('\u0B99', '\u0B9A', state5), new Transition('\u3400', '\u3400', state5),
				new Transition('\u4E00', '\u4E00', state5), new Transition('\uAC00', '\uAC00', state5),
				new Transition('\uD800', '\uD800', state0), new Transition('\uD869', '\uD869', state1),
				new Transition('\u1100', '\u1159', state5), new Transition('\u0B9C', '\u0B9C', state5),
				new Transition('\u0AD0', '\u0AD0', state5), new Transition('\u1200', '\u1206', state5),
				new Transition('\u1320', '\u1346', state5), new Transition('\u2135', '\u2138', state5),
				new Transition('\u0B36', '\u0B39', state5), new Transition('\u0B9E', '\u0B9F', state5),
				new Transition('\u1780', '\u17B3', state5), new Transition('\u0993', '\u09A8', state5),
				new Transition('\u3006', '\u3006', state5), new Transition('\uFB38', '\uFB3C', state5),
				new Transition('\u12D0', '\u12D6', state5), new Transition('\u0A05', '\u0A0A', state5),
				new Transition('\u06D5', '\u06D5', state5), new Transition('\uD7A3', '\uD7A3', state5),
				new Transition('\u0C05', '\u0C0C', state5), new Transition('\u30A1', '\u30FA', state5),
				new Transition('\u093D', '\u093D', state5), new Transition('\u0B3D', '\u0B3D', state5),
				new Transition('\u0BA3', '\u0BA4', state5), new Transition('\uFB3E', '\uFB3E', state5),
				new Transition('\uFE70', '\uFE72', state5), new Transition('\u9FA5', '\u9FA5', state5),
				new Transition('\u3041', '\u3094', state5), new Transition('\u0A72', '\u0A74', state5),
				new Transition('\u166F', '\u1676', state5), new Transition('\uFE74', '\uFE74', state5),
				new Transition('\u0D9A', '\u0DB1', state5), new Transition('\uFB40', '\uFB41', state5),
				new Transition('\u0C0E', '\u0C10', state5), new Transition('\u0A0F', '\u0A10', state5),
				new Transition('\u0BA8', '\u0BAA', state5), new Transition('\u0EDC', '\u0EDD', state5),
				new Transition('\u1260', '\u1286', state5), new Transition('\u0F40', '\u0F47', state5),
				new Transition('\u0CDE', '\u0CDE', state5), new Transition('\uFB43', '\uFB44', state5),
				new Transition('\u0AE0', '\u0AE0', state5), new Transition('\u0CE0', '\u0CE1', state5),
				new Transition('\u1000', '\u1021', state5), new Transition('\u09AA', '\u09B0', state5),
				new Transition('\u31A0', '\u31B7', state5), new Transition('\uD87E', '\uD87E', state2),
				new Transition('\u09B2', '\u09B2', state5), new Transition('\u12D8', '\u12EE', state5),
				new Transition('\u0BAE', '\u0BB5', state5), new Transition('\uFD92', '\uFDC7', state5),
				new Transition('\uFFA0', '\uFFBE', state5), new Transition('\u10D0', '\u10F6', state5),
				new Transition('\uF900', '\uFA2D', state5), new Transition('\u0E81', '\u0E82', state5),
				new Transition('\u4DB5', '\u4DB5', state5), new Transition('\u0950', '\u0950', state5),
				new Transition('\u0E01', '\u0E30', state5), new Transition('\u0E84', '\u0E84', state5),
				new Transition('\u09B6', '\u09B9', state5), new Transition('\u0DB3', '\u0DBB', state5),
				new Transition('\u0BB7', '\u0BB9', state5), new Transition('\u0C12', '\u0C28', state5),
				new Transition('\u0A13', '\u0A28', state5), new Transition('\u1348', '\u135A', state5),
				new Transition('\u0E87', '\u0E88', state5), new Transition('\u01BB', '\u01BB', state5),
				new Transition('\u1288', '\u1288', state5), new Transition('\u0A85', '\u0A8B', state5),
				new Transition('\u0C85', '\u0C8C', state5), new Transition('\u0DBD', '\u0DBD', state5),
				new Transition('\u0E8A', '\u0E8A', state5), new Transition('\u1023', '\u1027', state5),
				new Transition('\u128A', '\u128D', state5), new Transition('\u0A8D', '\u0A8D', state5),
				new Transition('\u0E8D', '\u0E8D', state5), new Transition('\u01C0', '\u01C3', state5),
				new Transition('\u0C8E', '\u0C90', state5), new Transition('\u0B5C', '\u0B5D', state5),
				new Transition('\u0F49', '\u0F6A', state5), new Transition('\u1029', '\u102A', state5),
				new Transition('\u1681', '\u169A', state5), new Transition('\u0A8F', '\u0A91', state5),
				new Transition('\u0DC0', '\u0DC6', state5), new Transition('\u0958', '\u0961', state5),
				new Transition('\uFFC2', '\uFFC7', state5), new Transition('\u0B5F', '\u0B61', state5),
				new Transition('\u1208', '\u1246', state5), new Transition('\u0D60', '\u0D61', state5),
				new Transition('\u0A2A', '\u0A30', state5), new Transition('\u06FA', '\u06FC', state5),
				new Transition('\u0C2A', '\u0C33', state5), new Transition('\u0E94', '\u0E97', state5),
				new Transition('\u0621', '\u063A', state5), new Transition('\u1880', '\u18A8', state5),
				new Transition('\u0A32', '\u0A33', state5), new Transition('\u0E32', '\u0E33', state5),
				new Transition('\u0F00', '\u0F00', state5), new Transition('\uFFCA', '\uFFCF', state5),
				new Transition('\u1820', '\u1842', state5), new Transition('\u12F0', '\u130E', state5),
				new Transition('\u0A35', '\u0A36', state5), new Transition('\u3131', '\u318E', state5),
				new Transition('\u0E99', '\u0E9F', state5), new Transition('\u0C35', '\u0C39', state5),
				new Transition('\u0A38', '\u0A39', state5), new Transition('\u0C92', '\u0CA8', state5),
				new Transition('\u13A0', '\u13F4', state5), new Transition('\uFF66', '\uFF6F', state5),
				new Transition('\u0A93', '\u0AA8', state5), new Transition('\u1290', '\u12AE', state5),
				new Transition('\u0EA1', '\u0EA3', state5), new Transition('\uFFD2', '\uFFD7', state5),
				new Transition('\u0B05', '\u0B0C', state5), new Transition('\u0D05', '\u0D0C', state5),
				new Transition('\u0EA5', '\u0EA5', state5), new Transition('\u11A8', '\u11F9', state5),
				new Transition('\u0EA7', '\u0EA7', state5), new Transition('\uD840', '\uD840', state3),
				new Transition('\u1401', '\u166C', state5), new Transition('\uFD50', '\uFD8F', state5),
				new Transition('\u0D0E', '\u0D10', state5), new Transition('\uFFDA', '\uFFDC', state5),
				new Transition('\u0B0F', '\u0B10', state5), new Transition('\u09DC', '\u09DD', state5),
				new Transition('\u0E40', '\u0E45', state5), new Transition('\u0710', '\u0710', state5),
				new Transition('\u1310', '\u1310', state5), new Transition('\u0EAA', '\u0EAB', state5),
				new Transition('\uFBD3', '\uFD3D', state5), new Transition('\u0671', '\u06D3', state5),
				new Transition('\u05D0', '\u05EA', state5), new Transition('\u0641', '\u064A', state5),
				new Transition('\u09DF', '\u09E1', state5), new Transition('\u1312', '\u1315', state5),
				new Transition('\u0AAA', '\u0AB0', state5), new Transition('\u1248', '\u1248', state5),
				new Transition('\u0EAD', '\u0EB0', state5), new Transition('\u0CAA', '\u0CB3', state5),
				new Transition('\u12B0', '\u12B0', state5), new Transition('\u124A', '\u124D', state5),
				new Transition('\u0AB2', '\u0AB3', state5), new Transition('\u0EB2', '\u0EB3', state5),
				new Transition('\u12B2', '\u12B5', state5), new Transition('\u1318', '\u131E', state5),
				new Transition('\u3105', '\u312C', state5), new Transition('\u0AB5', '\u0AB9', state5),
				new Transition('\u0CB5', '\u0CB9', state5), new Transition('\uFB1D', '\uFB1D', state5),
				new Transition('\uFB46', '\uFBB1', state5), new Transition('\u0D12', '\u0D28', state5),
				new Transition('\u0B13', '\u0B28', state5), new Transition('\u1050', '\u1055', state5),
				new Transition('\u1250', '\u1256', state5), new Transition('\u115F', '\u11A2', state5),
				new Transition('\uA000', '\uA48C', state5), new Transition('\u0712', '\u072C', state5),
				new Transition('\u0B85', '\u0B8A', state5), new Transition('\u12B8', '\u12BE', state5),
				new Transition('\u0985', '\u098C', state5), new Transition('\u0ABD', '\u0ABD', state5),
				new Transition('\u0EBD', '\u0EBD', state5), new Transition('\u0F88', '\u0F8B', state5),
				new Transition('\uFB1F', '\uFB28', state5), new Transition('\u09F0', '\u09F1', state5),
				new Transition('\u0905', '\u0939', state5), new Transition('\u05F0', '\u05F2', state5),
				new Transition('\u1258', '\u1258', state5), new Transition('\uFF71', '\uFF9D', state5),
				new Transition('\u12C0', '\u12C0', state5), new Transition('\u0A59', '\u0A5C', state5),
				new Transition('\u125A', '\u125D', state5), new Transition('\u0B8E', '\u0B90', state5),
				new Transition('\u0D85', '\u0D96', state5), new Transition('\u0EC0', '\u0EC4', state5),
				new Transition('\u098F', '\u0990', state5), new Transition('\u12C2', '\u12C5', state5),
				new Transition('\uFDF0', '\uFDFB', state5), new Transition('\u0A5E', '\u0A5E', state5),
				new Transition('\u0B92', '\u0B95', state5), new Transition('\u0C60', '\u0C61', state5),
				new Transition('\u0B2A', '\u0B30', state5), new Transition('\uFE76', '\uFEFC', state5),
				new Transition('\u1844', '\u1877', state5), new Transition('\u0780', '\u07A5', state5),
				new Transition('\uFB2A', '\uFB36', state5), new Transition('\u12C8', '\u12CE', state5),
				new Transition('\u0B32', '\u0B33', state5), new Transition('\u16A0', '\u16EA', state5),
				new Transition('\u0D2A', '\u0D39', state5));
		state5.alter(true, 1);
		return Automaton.load(state4, true, null);
	}

	private static Automaton getAut76() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u01CB', '\u01CB', state1), new Transition('\u01C8', '\u01C8', state1),
				new Transition('\u01C5', '\u01C5', state1), new Transition('\u01F2', '\u01F2', state1),
				new Transition('\u1FBC', '\u1FBC', state1), new Transition('\u1FCC', '\u1FCC', state1),
				new Transition('\u1FFC', '\u1FFC', state1), new Transition('\u1F88', '\u1F8F', state1),
				new Transition('\u1F98', '\u1F9F', state1), new Transition('\u1FA8', '\u1FAF', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut77() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDC25', state3));
		state1.alter(false, 1, new Transition('\u0200', '\u0200', state3), new Transition('\u1E00', '\u1E00', state3),
				new Transition('\uD801', '\uD801', state0), new Transition('\u00C0', '\u00D6', state3),
				new Transition('\u0134', '\u0134', state3), new Transition('\u0391', '\u03A1', state3),
				new Transition('\u1ECE', '\u1ECE', state3), new Transition('\u0468', '\u0468', state3),
				new Transition('\u1E68', '\u1E68', state3), new Transition('\u0202', '\u0202', state3),
				new Transition('\u1E02', '\u1E02', state3), new Transition('\u0136', '\u0136', state3),
				new Transition('\u019C', '\u019D', state3), new Transition('\u04D0', '\u04D0', state3),
				new Transition('\u1ED0', '\u1ED0', state3), new Transition('\u046A', '\u046A', state3),
				new Transition('\u1E6A', '\u1E6A', state3), new Transition('\u0204', '\u0204', state3),
				new Transition('\u1E04', '\u1E04', state3), new Transition('\u04D2', '\u04D2', state3),
				new Transition('\u1ED2', '\u1ED2', state3), new Transition('\u046C', '\u046C', state3),
				new Transition('\u1E6C', '\u1E6C', state3), new Transition('\u0139', '\u0139', state3),
				new Transition('\u019F', '\u01A0', state3), new Transition('\u0206', '\u0206', state3),
				new Transition('\u1E06', '\u1E06', state3), new Transition('\u04D4', '\u04D4', state3),
				new Transition('\u1ED4', '\u1ED4', state3), new Transition('\u046E', '\u046E', state3),
				new Transition('\u1E6E', '\u1E6E', state3), new Transition('\u013B', '\u013B', state3),
				new Transition('\u0208', '\u0208', state3), new Transition('\u1E08', '\u1E08', state3),
				new Transition('\u01A2', '\u01A2', state3), new Transition('\u1F38', '\u1F3F', state3),
				new Transition('\u04D6', '\u04D6', state3), new Transition('\u1ED6', '\u1ED6', state3),
				new Transition('\u0470', '\u0470', state3), new Transition('\u1E70', '\u1E70', state3),
				new Transition('\u013D', '\u013D', state3), new Transition('\u020A', '\u020A', state3),
				new Transition('\u1E0A', '\u1E0A', state3), new Transition('\u01A4', '\u01A4', state3),
				new Transition('\u04D8', '\u04D8', state3), new Transition('\u1ED8', '\u1ED8', state3),
				new Transition('\u0472', '\u0472', state3), new Transition('\u1E72', '\u1E72', state3),
				new Transition('\u013F', '\u013F', state3), new Transition('\u020C', '\u020C', state3),
				new Transition('\u1E0C', '\u1E0C', state3), new Transition('\u01A6', '\u01A7', state3),
				new Transition('\u04DA', '\u04DA', state3), new Transition('\u1EDA', '\u1EDA', state3),
				new Transition('\u0474', '\u0474', state3), new Transition('\u1E74', '\u1E74', state3),
				new Transition('\u0141', '\u0141', state3), new Transition('\u020E', '\u020E', state3),
				new Transition('\u1E0E', '\u1E0E', state3), new Transition('\u03A3', '\u03AB', state3),
				new Transition('\u00D8', '\u00DE', state3), new Transition('\u04DC', '\u04DC', state3),
				new Transition('\u1EDC', '\u1EDC', state3), new Transition('\u01A9', '\u01A9', state3),
				new Transition('\u0476', '\u0476', state3), new Transition('\u1E76', '\u1E76', state3),
				new Transition('\u0143', '\u0143', state3), new Transition('\u0210', '\u0210', state3),
				new Transition('\u1E10', '\u1E10', state3), new Transition('\u04DE', '\u04DE', state3),
				new Transition('\u1EDE', '\u1EDE', state3), new Transition('\u0478', '\u0478', state3),
				new Transition('\u1E78', '\u1E78', state3), new Transition('\u0145', '\u0145', state3),
				new Transition('\u0212', '\u0212', state3), new Transition('\u1E12', '\u1E12', state3),
				new Transition('\u01AC', '\u01AC', state3), new Transition('\u04E0', '\u04E0', state3),
				new Transition('\u1EE0', '\u1EE0', state3), new Transition('\u047A', '\u047A', state3),
				new Transition('\u1E7A', '\u1E7A', state3), new Transition('\u0147', '\u0147', state3),
				new Transition('\u0214', '\u0214', state3), new Transition('\u0531', '\u0556', state3),
				new Transition('\u1E14', '\u1E14', state3), new Transition('\u01AE', '\u01AF', state3),
				new Transition('\u04E2', '\u04E2', state3), new Transition('\u1EE2', '\u1EE2', state3),
				new Transition('\u047C', '\u047C', state3), new Transition('\u1E7C', '\u1E7C', state3),
				new Transition('\u0216', '\u0216', state3), new Transition('\u1E16', '\u1E16', state3),
				new Transition('\u014A', '\u014A', state3), new Transition('\u04E4', '\u04E4', state3),
				new Transition('\u1EE4', '\u1EE4', state3), new Transition('\u047E', '\u047E', state3),
				new Transition('\u1E7E', '\u1E7E', state3), new Transition('\u1F48', '\u1F4D', state3),
				new Transition('\u0218', '\u0218', state3), new Transition('\u1E18', '\u1E18', state3),
				new Transition('\u01B1', '\u01B3', state3), new Transition('\u014C', '\u014C', state3),
				new Transition('\u04E6', '\u04E6', state3), new Transition('\u1EE6', '\u1EE6', state3),
				new Transition('\u0480', '\u0480', state3), new Transition('\u1E80', '\u1E80', state3),
				new Transition('\u021A', '\u021A', state3), new Transition('\u1E1A', '\u1E1A', state3),
				new Transition('\u014E', '\u014E', state3), new Transition('\u04E8', '\u04E8', state3),
				new Transition('\u1EE8', '\u1EE8', state3), new Transition('\u01B5', '\u01B5', state3),
				new Transition('\u1E82', '\u1E82', state3), new Transition('\u021C', '\u021C', state3),
				new Transition('\u1E1C', '\u1E1C', state3), new Transition('\u0400', '\u042F', state3),
				new Transition('\u0150', '\u0150', state3), new Transition('\u04EA', '\u04EA', state3),
				new Transition('\u1EEA', '\u1EEA', state3), new Transition('\u1E84', '\u1E84', state3),
				new Transition('\u01B7', '\u01B8', state3), new Transition('\u021E', '\u021E', state3),
				new Transition('\u1E1E', '\u1E1E', state3), new Transition('\u0152', '\u0152', state3),
				new Transition('\u04EC', '\u04EC', state3), new Transition('\u1EEC', '\u1EEC', state3),
				new Transition('\u1E86', '\u1E86', state3), new Transition('\u1E20', '\u1E20', state3),
				new Transition('\u1FB8', '\u1FBB', state3), new Transition('\u0154', '\u0154', state3),
				new Transition('\u04EE', '\u04EE', state3), new Transition('\u1EEE', '\u1EEE', state3),
				new Transition('\u1E88', '\u1E88', state3), new Transition('\u0222', '\u0222', state3),
				new Transition('\u1E22', '\u1E22', state3), new Transition('\u01BC', '\u01BC', state3),
				new Transition('\u0156', '\u0156', state3), new Transition('\u04F0', '\u04F0', state3),
				new Transition('\u1EF0', '\u1EF0', state3), new Transition('\u1E8A', '\u1E8A', state3),
				new Transition('\u0224', '\u0224', state3), new Transition('\u1E24', '\u1E24', state3),
				new Transition('\u0158', '\u0158', state3), new Transition('\u04F2', '\u04F2', state3),
				new Transition('\u1EF2', '\u1EF2', state3), new Transition('\u048C', '\u048C', state3),
				new Transition('\u1E8C', '\u1E8C', state3), new Transition('\u1F59', '\u1F59', state3),
				new Transition('\u0226', '\u0226', state3), new Transition('\u1E26', '\u1E26', state3),
				new Transition('\u015A', '\u015A', state3), new Transition('\u04F4', '\u04F4', state3),
				new Transition('\u1EF4', '\u1EF4', state3), new Transition('\u048E', '\u048E', state3),
				new Transition('\u1E8E', '\u1E8E', state3), new Transition('\u1F5B', '\u1F5B', state3),
				new Transition('\u0228', '\u0228', state3), new Transition('\u1E28', '\u1E28', state3),
				new Transition('\u015C', '\u015C', state3), new Transition('\u1EF6', '\u1EF6', state3),
				new Transition('\u0490', '\u0490', state3), new Transition('\u1E90', '\u1E90', state3),
				new Transition('\u1F5D', '\u1F5D', state3), new Transition('\u022A', '\u022A', state3),
				new Transition('\u1E2A', '\u1E2A', state3), new Transition('\u01C4', '\u01C4', state3),
				new Transition('\u015E', '\u015E', state3), new Transition('\u04F8', '\u04F8', state3),
				new Transition('\u1EF8', '\u1EF8', state3), new Transition('\u0492', '\u0492', state3),
				new Transition('\u1E92', '\u1E92', state3), new Transition('\u1F5F', '\u1F5F', state3),
				new Transition('\u022C', '\u022C', state3), new Transition('\u1E2C', '\u1E2C', state3),
				new Transition('\u0160', '\u0160', state3), new Transition('\u01C7', '\u01C7', state3),
				new Transition('\u0494', '\u0494', state3), new Transition('\u1E94', '\u1E94', state3),
				new Transition('\u022E', '\u022E', state3), new Transition('\u1E2E', '\u1E2E', state3),
				new Transition('\u0162', '\u0162', state3), new Transition('\u0496', '\u0496', state3),
				new Transition('\u0230', '\u0230', state3), new Transition('\u1E30', '\u1E30', state3),
				new Transition('\u1FC8', '\u1FCB', state3), new Transition('\u01CA', '\u01CA', state3),
				new Transition('\u0164', '\u0164', state3), new Transition('\u0498', '\u0498', state3),
				new Transition('\u0232', '\u0232', state3), new Transition('\u1E32', '\u1E32', state3),
				new Transition('\u0166', '\u0166', state3), new Transition('\u0100', '\u0100', state3),
				new Transition('\u01CD', '\u01CD', state3), new Transition('\u049A', '\u049A', state3),
				new Transition('\u1E34', '\u1E34', state3), new Transition('\u0168', '\u0168', state3),
				new Transition('\u0102', '\u0102', state3), new Transition('\u2102', '\u2102', state3),
				new Transition('\u01CF', '\u01CF', state3), new Transition('\u049C', '\u049C', state3),
				new Transition('\uD835', '\uD835', state2), new Transition('\u1E36', '\u1E36', state3),
				new Transition('\u016A', '\u016A', state3), new Transition('\u0104', '\u0104', state3),
				new Transition('\u01D1', '\u01D1', state3), new Transition('\u049E', '\u049E', state3),
				new Transition('\u1E38', '\u1E38', state3), new Transition('\u016C', '\u016C', state3),
				new Transition('\u1F68', '\u1F6F', state3), new Transition('\u0106', '\u0106', state3),
				new Transition('\u01D3', '\u01D3', state3), new Transition('\u03D2', '\u03D4', state3),
				new Transition('\u04A0', '\u04A0', state3), new Transition('\u1EA0', '\u1EA0', state3),
				new Transition('\u1E3A', '\u1E3A', state3), new Transition('\u2107', '\u2107', state3),
				new Transition('\u016E', '\u016E', state3), new Transition('\u0108', '\u0108', state3),
				new Transition('\u01D5', '\u01D5', state3), new Transition('\u04A2', '\u04A2', state3),
				new Transition('\u1EA2', '\u1EA2', state3), new Transition('\u1E3C', '\u1E3C', state3),
				new Transition('\u0170', '\u0170', state3), new Transition('\u010A', '\u010A', state3),
				new Transition('\u01D7', '\u01D7', state3), new Transition('\u04A4', '\u04A4', state3),
				new Transition('\u1EA4', '\u1EA4', state3), new Transition('\u1E3E', '\u1E3E', state3),
				new Transition('\u0172', '\u0172', state3), new Transition('\u010C', '\u010C', state3),
				new Transition('\u01D9', '\u01D9', state3), new Transition('\u1F08', '\u1F0F', state3),
				new Transition('\u210B', '\u210D', state3), new Transition('\u04A6', '\u04A6', state3),
				new Transition('\u1EA6', '\u1EA6', state3), new Transition('\u1E40', '\u1E40', state3),
				new Transition('\u1FD8', '\u1FDB', state3), new Transition('\u03DA', '\u03DA', state3),
				new Transition('\u0174', '\u0174', state3), new Transition('\u010E', '\u010E', state3),
				new Transition('\u01DB', '\u01DB', state3), new Transition('\u04A8', '\u04A8', state3),
				new Transition('\u1EA8', '\u1EA8', state3), new Transition('\u1E42', '\u1E42', state3),
				new Transition('\u03DC', '\u03DC', state3), new Transition('\u0176', '\u0176', state3),
				new Transition('\u0110', '\u0110', state3), new Transition('\u04AA', '\u04AA', state3),
				new Transition('\u1EAA', '\u1EAA', state3), new Transition('\u1E44', '\u1E44', state3),
				new Transition('\u01DE', '\u01DE', state3), new Transition('\u03DE', '\u03DE', state3),
				new Transition('\u2110', '\u2112', state3), new Transition('\u0112', '\u0112', state3),
				new Transition('\u0178', '\u0179', state3), new Transition('\u04AC', '\u04AC', state3),
				new Transition('\u1EAC', '\u1EAC', state3), new Transition('\u1E46', '\u1E46', state3),
				new Transition('\u01E0', '\u01E0', state3), new Transition('\u03E0', '\u03E0', state3),
				new Transition('\u0114', '\u0114', state3), new Transition('\u04AE', '\u04AE', state3),
				new Transition('\u1EAE', '\u1EAE', state3), new Transition('\u017B', '\u017B', state3),
				new Transition('\u1E48', '\u1E48', state3), new Transition('\u2115', '\u2115', state3),
				new Transition('\u01E2', '\u01E2', state3), new Transition('\u03E2', '\u03E2', state3),
				new Transition('\u0116', '\u0116', state3), new Transition('\u04B0', '\u04B0', state3),
				new Transition('\u1EB0', '\u1EB0', state3), new Transition('\u017D', '\u017D', state3),
				new Transition('\u1E4A', '\u1E4A', state3), new Transition('\u01E4', '\u01E4', state3),
				new Transition('\u03E4', '\u03E4', state3), new Transition('\u0118', '\u0118', state3),
				new Transition('\u04B2', '\u04B2', state3), new Transition('\u1EB2', '\u1EB2', state3),
				new Transition('\u1E4C', '\u1E4C', state3), new Transition('\u01E6', '\u01E6', state3),
				new Transition('\u03E6', '\u03E6', state3), new Transition('\u011A', '\u011A', state3),
				new Transition('\u04B4', '\u04B4', state3), new Transition('\u1EB4', '\u1EB4', state3),
				new Transition('\u1E4E', '\u1E4E', state3), new Transition('\u1F18', '\u1F1D', state3),
				new Transition('\u0181', '\u0182', state3), new Transition('\u01E8', '\u01E8', state3),
				new Transition('\u03E8', '\u03E8', state3), new Transition('\u2119', '\u211D', state3),
				new Transition('\u011C', '\u011C', state3), new Transition('\u04B6', '\u04B6', state3),
				new Transition('\u1EB6', '\u1EB6', state3), new Transition('\u10A0', '\u10C5', state3),
				new Transition('\u0041', '\u005A', state3), new Transition('\u1E50', '\u1E50', state3),
				new Transition('\u01EA', '\u01EA', state3), new Transition('\u03EA', '\u03EA', state3),
				new Transition('\u0184', '\u0184', state3), new Transition('\u1FE8', '\u1FEC', state3),
				new Transition('\u011E', '\u011E', state3), new Transition('\u04B8', '\u04B8', state3),
				new Transition('\u1EB8', '\u1EB8', state3), new Transition('\u1E52', '\u1E52', state3),
				new Transition('\u01EC', '\u01EC', state3), new Transition('\u03EC', '\u03EC', state3),
				new Transition('\u0386', '\u0386', state3), new Transition('\u0120', '\u0120', state3),
				new Transition('\u0186', '\u0187', state3), new Transition('\u04BA', '\u04BA', state3),
				new Transition('\u1EBA', '\u1EBA', state3), new Transition('\u1E54', '\u1E54', state3),
				new Transition('\u01EE', '\u01EE', state3), new Transition('\u03EE', '\u03EE', state3),
				new Transition('\u0122', '\u0122', state3), new Transition('\u04BC', '\u04BC', state3),
				new Transition('\u1EBC', '\u1EBC', state3), new Transition('\u0388', '\u038A', state3),
				new Transition('\u1E56', '\u1E56', state3), new Transition('\u0189', '\u018B', state3),
				new Transition('\u0124', '\u0124', state3), new Transition('\u2124', '\u2124', state3),
				new Transition('\u01F1', '\u01F1', state3), new Transition('\u04BE', '\u04BE', state3),
				new Transition('\u1EBE', '\u1EBE', state3), new Transition('\u1E58', '\u1E58', state3),
				new Transition('\u038C', '\u038C', state3), new Transition('\u0126', '\u0126', state3),
				new Transition('\u2126', '\u2126', state3), new Transition('\u1EC0', '\u1EC0', state3),
				new Transition('\u1E5A', '\u1E5A', state3), new Transition('\u04C0', '\u04C1', state3),
				new Transition('\u01F4', '\u01F4', state3), new Transition('\u03F4', '\u03F4', state3),
				new Transition('\u0128', '\u0128', state3), new Transition('\u2128', '\u2128', state3),
				new Transition('\u038E', '\u038F', state3), new Transition('\u1EC2', '\u1EC2', state3),
				new Transition('\u1E5C', '\u1E5C', state3), new Transition('\u018E', '\u0191', state3),
				new Transition('\u04C3', '\u04C3', state3), new Transition('\u012A', '\u012A', state3),
				new Transition('\u01F6', '\u01F8', state3), new Transition('\u1EC4', '\u1EC4', state3),
				new Transition('\u1E5E', '\u1E5E', state3), new Transition('\u212A', '\u212D', state3),
				new Transition('\u012C', '\u012C', state3), new Transition('\u1F28', '\u1F2F', state3),
				new Transition('\u1EC6', '\u1EC6', state3), new Transition('\u0460', '\u0460', state3),
				new Transition('\u1E60', '\u1E60', state3), new Transition('\u1FF8', '\u1FFB', state3),
				new Transition('\u0193', '\u0194', state3), new Transition('\u01FA', '\u01FA', state3),
				new Transition('\u04C7', '\u04C7', state3), new Transition('\u012E', '\u012E', state3),
				new Transition('\u1EC8', '\u1EC8', state3), new Transition('\u0462', '\u0462', state3),
				new Transition('\u1E62', '\u1E62', state3), new Transition('\u01FC', '\u01FC', state3),
				new Transition('\u0130', '\u0130', state3), new Transition('\u1ECA', '\u1ECA', state3),
				new Transition('\u2130', '\u2131', state3), new Transition('\u0196', '\u0198', state3),
				new Transition('\u0464', '\u0464', state3), new Transition('\u1E64', '\u1E64', state3),
				new Transition('\uFF21', '\uFF3A', state3), new Transition('\u01FE', '\u01FE', state3),
				new Transition('\u04CB', '\u04CB', state3), new Transition('\u0132', '\u0132', state3),
				new Transition('\u1ECC', '\u1ECC', state3), new Transition('\u0466', '\u0466', state3),
				new Transition('\u1E66', '\u1E66', state3), new Transition('\u2133', '\u2133', state3));
		state2.alter(false, 3, new Transition('\uDD4A', '\uDD50', state3), new Transition('\uDD16', '\uDD1C', state3),
				new Transition('\uDC9C', '\uDC9C', state3), new Transition('\uDD40', '\uDD44', state3),
				new Transition('\uDC34', '\uDC4D', state3), new Transition('\uDC00', '\uDC19', state3),
				new Transition('\uDF1C', '\uDF34', state3), new Transition('\uDCA9', '\uDCAC', state3),
				new Transition('\uDD0D', '\uDD14', state3), new Transition('\uDD04', '\uDD05', state3),
				new Transition('\uDEA8', '\uDEC0', state3), new Transition('\uDC68', '\uDC81', state3),
				new Transition('\uDD46', '\uDD46', state3), new Transition('\uDF90', '\uDFA8', state3),
				new Transition('\uDC9E', '\uDC9F', state3), new Transition('\uDCD0', '\uDCE9', state3),
				new Transition('\uDD38', '\uDD39', state3), new Transition('\uDD6C', '\uDD85', state3),
				new Transition('\uDD07', '\uDD0A', state3), new Transition('\uDDD4', '\uDDED', state3),
				new Transition('\uDCA2', '\uDCA2', state3), new Transition('\uDDA0', '\uDDB9', state3),
				new Transition('\uDF56', '\uDF6E', state3), new Transition('\uDE3C', '\uDE55', state3),
				new Transition('\uDD3B', '\uDD3E', state3), new Transition('\uDEE2', '\uDEFA', state3),
				new Transition('\uDE08', '\uDE21', state3), new Transition('\uDCA5', '\uDCA6', state3),
				new Transition('\uDCAE', '\uDCB5', state3), new Transition('\uDE70', '\uDE89', state3));
		state3.alter(true, 2);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut78() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 1, new Transition('\uDD65', '\uDD69', state2), new Transition('\uDD6D', '\uDD72', state2),
				new Transition('\uDD7B', '\uDD82', state2), new Transition('\uDD85', '\uDD8B', state2),
				new Transition('\uDDAA', '\uDDAD', state2));
		state1.alter(false, 0, new Transition('\uD834', '\uD834', state0), new Transition('\u3099', '\u309A', state2),
				new Transition('\u0591', '\u05A1', state2), new Transition('\u0F35', '\u0F35', state2),
				new Transition('\u0A02', '\u0A02', state2), new Transition('\u0901', '\u0903', state2),
				new Transition('\u0B01', '\u0B03', state2), new Transition('\u0C01', '\u0C03', state2),
				new Transition('\u0D02', '\u0D03', state2), new Transition('\u0F37', '\u0F37', state2),
				new Transition('\u0E34', '\u0E3A', state2), new Transition('\u1036', '\u1039', state2),
				new Transition('\u0DCF', '\u0DD4', state2), new Transition('\u0F39', '\u0F39', state2),
				new Transition('\u093C', '\u093C', state2), new Transition('\u0A3C', '\u0A3C', state2),
				new Transition('\u0B3C', '\u0B3C', state2), new Transition('\u0CD5', '\u0CD6', state2),
				new Transition('\u0DD6', '\u0DD6', state2), new Transition('\u0670', '\u0670', state2),
				new Transition('\u09D7', '\u09D7', state2), new Transition('\u0A70', '\u0A71', state2),
				new Transition('\u0BD7', '\u0BD7', state2), new Transition('\u0F3E', '\u0F3F', state2),
				new Transition('\u0730', '\u074A', state2), new Transition('\u0A3E', '\u0A42', state2),
				new Transition('\u0B3E', '\u0B43', state2), new Transition('\u0D3E', '\u0D43', state2),
				new Transition('\u0C3E', '\u0C44', state2), new Transition('\u20D0', '\u20E3', state2),
				new Transition('\u0DD8', '\u0DDF', state2), new Transition('\u18A9', '\u18A9', state2),
				new Transition('\u0711', '\u0711', state2), new Transition('\u06D6', '\u06E4', state2),
				new Transition('\u07A6', '\u07B0', state2), new Transition('\u093E', '\u094D', state2),
				new Transition('\u0C46', '\u0C48', state2), new Transition('\u0D46', '\u0D48', state2),
				new Transition('\u0A47', '\u0A48', state2), new Transition('\u0B47', '\u0B48', state2),
				new Transition('\u0F99', '\u0FBC', state2), new Transition('\u09E2', '\u09E3', state2),
				new Transition('\u0F71', '\u0F84', state2), new Transition('\u05A3', '\u05B9', state2),
				new Transition('\u0EB1', '\u0EB1', state2), new Transition('\u0E47', '\u0E4E', state2),
				new Transition('\u0C4A', '\u0C4D', state2), new Transition('\u0D4A', '\u0D4D', state2),
				new Transition('\u0F18', '\u0F19', state2), new Transition('\u0A4B', '\u0A4D', state2),
				new Transition('\u0B4B', '\u0B4D', state2), new Transition('\u06E7', '\u06E8', state2),
				new Transition('\u0981', '\u0983', state2), new Transition('\u0A81', '\u0A83', state2),
				new Transition('\u0B82', '\u0B83', state2), new Transition('\u0C82', '\u0C83', state2),
				new Transition('\u0D82', '\u0D83', state2), new Transition('\uFB1E', '\uFB1E', state2),
				new Transition('\u0EB4', '\u0EB9', state2), new Transition('\u064B', '\u0655', state2),
				new Transition('\u0483', '\u0486', state2), new Transition('\u06EA', '\u06ED', state2),
				new Transition('\u0951', '\u0954', state2), new Transition('\u0F86', '\u0F87', state2),
				new Transition('\u0EBB', '\u0EBC', state2), new Transition('\u0488', '\u0489', state2),
				new Transition('\u09BC', '\u09BC', state2), new Transition('\u0ABC', '\u0ABC', state2),
				new Transition('\u0C55', '\u0C56', state2), new Transition('\u05BB', '\u05BD', state2),
				new Transition('\uFE20', '\uFE23', state2), new Transition('\u0B56', '\u0B57', state2),
				new Transition('\u0D57', '\u0D57', state2), new Transition('\u1056', '\u1059', state2),
				new Transition('\u05BF', '\u05BF', state2), new Transition('\u0DF2', '\u0DF3', state2),
				new Transition('\u0BBE', '\u0BC2', state2), new Transition('\u05C1', '\u05C2', state2),
				new Transition('\u09BE', '\u09C4', state2), new Transition('\u0CBE', '\u0CC4', state2),
				new Transition('\u0ABE', '\u0AC5', state2), new Transition('\u05C4', '\u05C4', state2),
				new Transition('\u0FC6', '\u0FC6', state2), new Transition('\u17B4', '\u17D3', state2),
				new Transition('\u302A', '\u302F', state2), new Transition('\u0BC6', '\u0BC8', state2),
				new Transition('\u0CC6', '\u0CC8', state2), new Transition('\u0F90', '\u0F97', state2),
				new Transition('\u0360', '\u0362', state2), new Transition('\u09C7', '\u09C8', state2),
				new Transition('\u0AC7', '\u0AC9', state2), new Transition('\u0300', '\u034E', state2),
				new Transition('\u0962', '\u0963', state2), new Transition('\u102C', '\u1032', state2),
				new Transition('\u0DCA', '\u0DCA', state2), new Transition('\u0E31', '\u0E31', state2),
				new Transition('\u0EC8', '\u0ECD', state2), new Transition('\u0BCA', '\u0BCD', state2),
				new Transition('\u0CCA', '\u0CCD', state2), new Transition('\u09CB', '\u09CD', state2),
				new Transition('\u0ACB', '\u0ACD', state2));
		state2.alter(true, 2);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut79() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0D00', '\u0D7F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut80() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDFFF', state2));
		state1.alter(false, 0, new Transition('\uD835', '\uD835', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut81() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2200', '\u22FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut82() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\uDD6D', '\uDD72', state2), new Transition('\uDD65', '\uDD66', state2));
		state1.alter(false, 1, new Transition('\uD834', '\uD834', state0), new Transition('\u17B4', '\u17B6', state2),
				new Transition('\u0C01', '\u0C03', state2), new Transition('\u0982', '\u0983', state2),
				new Transition('\u0B02', '\u0B03', state2), new Transition('\u0C82', '\u0C83', state2),
				new Transition('\u0D02', '\u0D03', state2), new Transition('\u0D82', '\u0D83', state2),
				new Transition('\u0903', '\u0903', state2), new Transition('\u0A83', '\u0A83', state2),
				new Transition('\u0B83', '\u0B83', state2), new Transition('\u0DCF', '\u0DD1', state2),
				new Transition('\u1038', '\u1038', state2), new Transition('\u0CD5', '\u0CD6', state2),
				new Transition('\u1056', '\u1057', state2), new Transition('\u09D7', '\u09D7', state2),
				new Transition('\u0B57', '\u0B57', state2), new Transition('\u0BD7', '\u0BD7', state2),
				new Transition('\u0D57', '\u0D57', state2), new Transition('\u0B3E', '\u0B3E', state2),
				new Transition('\u0CBE', '\u0CBE', state2), new Transition('\u0BBE', '\u0BBF', state2),
				new Transition('\u0F3E', '\u0F3F', state2), new Transition('\u093E', '\u0940', state2),
				new Transition('\u09BE', '\u09C0', state2), new Transition('\u0A3E', '\u0A40', state2),
				new Transition('\u0ABE', '\u0AC0', state2), new Transition('\u0D3E', '\u0D40', state2),
				new Transition('\u0DF2', '\u0DF3', state2), new Transition('\u0B40', '\u0B40', state2),
				new Transition('\u0BC1', '\u0BC2', state2), new Transition('\u17BE', '\u17C5', state2),
				new Transition('\u0CC0', '\u0CC4', state2), new Transition('\u0DD8', '\u0DDF', state2),
				new Transition('\u0C41', '\u0C44', state2), new Transition('\u102C', '\u102C', state2),
				new Transition('\u0BC6', '\u0BC8', state2), new Transition('\u0D46', '\u0D48', state2),
				new Transition('\u09C7', '\u09C8', state2), new Transition('\u0B47', '\u0B48', state2),
				new Transition('\u0CC7', '\u0CC8', state2), new Transition('\u17C7', '\u17C8', state2),
				new Transition('\u0AC9', '\u0AC9', state2), new Transition('\u0CCA', '\u0CCB', state2),
				new Transition('\u1031', '\u1031', state2), new Transition('\u0949', '\u094C', state2),
				new Transition('\u0BCA', '\u0BCC', state2), new Transition('\u0D4A', '\u0D4C', state2),
				new Transition('\u09CB', '\u09CC', state2), new Transition('\u0ACB', '\u0ACC', state2),
				new Transition('\u0B4B', '\u0B4C', state2), new Transition('\u0F7F', '\u0F7F', state2));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut83() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u06DD', '\u06DE', state1), new Transition('\u20DD', '\u20E0', state1),
				new Transition('\u0488', '\u0489', state1), new Transition('\u20E2', '\u20E3', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut84() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2600', '\u26FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut85() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2300', '\u23FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut86() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\uDD7B', '\uDD82', state2), new Transition('\uDD67', '\uDD69', state2),
				new Transition('\uDD85', '\uDD8B', state2), new Transition('\uDDAA', '\uDDAD', state2));
		state1.alter(false, 1, new Transition('\u3099', '\u309A', state2), new Transition('\uD834', '\uD834', state0),
				new Transition('\u094D', '\u094D', state2), new Transition('\u09CD', '\u09CD', state2),
				new Transition('\u0ACD', '\u0ACD', state2), new Transition('\u0B4D', '\u0B4D', state2),
				new Transition('\u0BCD', '\u0BCD', state2), new Transition('\u0D4D', '\u0D4D', state2),
				new Transition('\u0591', '\u05A1', state2), new Transition('\u0981', '\u0981', state2),
				new Transition('\u0B01', '\u0B01', state2), new Transition('\u06E7', '\u06E8', state2),
				new Transition('\u0901', '\u0902', state2), new Transition('\u0A81', '\u0A82', state2),
				new Transition('\u0F35', '\u0F35', state2), new Transition('\u0A02', '\u0A02', state2),
				new Transition('\u0B82', '\u0B82', state2), new Transition('\u17C9', '\u17D3', state2),
				new Transition('\u0F80', '\u0F84', state2), new Transition('\u1036', '\u1037', state2),
				new Transition('\uFB1E', '\uFB1E', state2), new Transition('\u0EB4', '\u0EB9', state2),
				new Transition('\u0F37', '\u0F37', state2), new Transition('\u064B', '\u0655', state2),
				new Transition('\u0E34', '\u0E3A', state2), new Transition('\u0483', '\u0486', state2),
				new Transition('\u06EA', '\u06ED', state2), new Transition('\u0F39', '\u0F39', state2),
				new Transition('\u1039', '\u1039', state2), new Transition('\u0951', '\u0954', state2),
				new Transition('\u0DD2', '\u0DD4', state2), new Transition('\u0F86', '\u0F87', state2),
				new Transition('\u17B7', '\u17BD', state2), new Transition('\u0EBB', '\u0EBC', state2),
				new Transition('\u093C', '\u093C', state2), new Transition('\u09BC', '\u09BC', state2),
				new Transition('\u0A3C', '\u0A3C', state2), new Transition('\u0ABC', '\u0ABC', state2),
				new Transition('\u0B3C', '\u0B3C', state2), new Transition('\u0C55', '\u0C56', state2),
				new Transition('\u05BB', '\u05BD', state2), new Transition('\uFE20', '\uFE23', state2),
				new Transition('\u0B56', '\u0B56', state2), new Transition('\u0DD6', '\u0DD6', state2),
				new Transition('\u0670', '\u0670', state2), new Transition('\u0A70', '\u0A71', state2),
				new Transition('\u20D0', '\u20DC', state2), new Transition('\u05BF', '\u05BF', state2),
				new Transition('\u0B3F', '\u0B3F', state2), new Transition('\u0CBF', '\u0CBF', state2),
				new Transition('\u1058', '\u1059', state2), new Transition('\u0C3E', '\u0C40', state2),
				new Transition('\u0730', '\u074A', state2), new Transition('\u06D6', '\u06DC', state2),
				new Transition('\u0BC0', '\u0BC0', state2), new Transition('\u05C1', '\u05C2', state2),
				new Transition('\u0A41', '\u0A42', state2), new Transition('\u0B41', '\u0B43', state2),
				new Transition('\u0D41', '\u0D43', state2), new Transition('\u18A9', '\u18A9', state2),
				new Transition('\u09C1', '\u09C4', state2), new Transition('\u0AC1', '\u0AC5', state2),
				new Transition('\u05C4', '\u05C4', state2), new Transition('\u0711', '\u0711', state2),
				new Transition('\u0941', '\u0948', state2), new Transition('\u07A6', '\u07B0', state2),
				new Transition('\u0F71', '\u0F7E', state2), new Transition('\u0CC6', '\u0CC6', state2),
				new Transition('\u0FC6', '\u0FC6', state2), new Transition('\u17C6', '\u17C6', state2),
				new Transition('\u302A', '\u302F', state2), new Transition('\u0C46', '\u0C48', state2),
				new Transition('\u0F90', '\u0F97', state2), new Transition('\u20E1', '\u20E1', state2),
				new Transition('\u0360', '\u0362', state2), new Transition('\u0A47', '\u0A48', state2),
				new Transition('\u0AC7', '\u0AC8', state2), new Transition('\u0F99', '\u0FBC', state2),
				new Transition('\u0300', '\u034E', state2), new Transition('\u06DF', '\u06E4', state2),
				new Transition('\u102D', '\u1030', state2), new Transition('\u0962', '\u0963', state2),
				new Transition('\u09E2', '\u09E3', state2), new Transition('\u05A3', '\u05B9', state2),
				new Transition('\u0DCA', '\u0DCA', state2), new Transition('\u0E31', '\u0E31', state2),
				new Transition('\u0EB1', '\u0EB1', state2), new Transition('\u0EC8', '\u0ECD', state2),
				new Transition('\u0E47', '\u0E4E', state2), new Transition('\u1032', '\u1032', state2),
				new Transition('\u0C4A', '\u0C4D', state2), new Transition('\u0F18', '\u0F19', state2),
				new Transition('\u0A4B', '\u0A4D', state2), new Transition('\u0CCC', '\u0CCD', state2));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut87() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1800', '\u18AF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut88() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDD00', '\uDDFF', state2));
		state1.alter(false, 0, new Transition('\uD834', '\uD834', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut89() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1000', '\u109F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut90() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 3, new Transition('\uDFCE', '\uDFFF', state3));
		state1.alter(false, 0, new Transition('\uDF4A', '\uDF4A', state3), new Transition('\uDF20', '\uDF23', state3));
		state2.alter(false, 1, new Transition('\uD800', '\uD800', state1), new Transition('\u1369', '\u137C', state3),
				new Transition('\u0030', '\u0039', state3), new Transition('\u06F0', '\u06F9', state3),
				new Transition('\uD835', '\uD835', state0), new Transition('\u2460', '\u249B', state3),
				new Transition('\u24EA', '\u24EA', state3), new Transition('\u09F4', '\u09F9', state3),
				new Transition('\u2074', '\u2079', state3), new Transition('\u0966', '\u096F', state3),
				new Transition('\u09E6', '\u09EF', state3), new Transition('\u0A66', '\u0A6F', state3),
				new Transition('\u0AE6', '\u0AEF', state3), new Transition('\u0B66', '\u0B6F', state3),
				new Transition('\u0C66', '\u0C6F', state3), new Transition('\u0CE6', '\u0CEF', state3),
				new Transition('\u0D66', '\u0D6F', state3), new Transition('\u0F20', '\u0F33', state3),
				new Transition('\u1040', '\u1049', state3), new Transition('\u2080', '\u2089', state3),
				new Transition('\u3280', '\u3289', state3), new Transition('\u00B9', '\u00B9', state3),
				new Transition('\u3038', '\u303A', state3), new Transition('\u3007', '\u3007', state3),
				new Transition('\u3192', '\u3195', state3), new Transition('\u0BE7', '\u0BF2', state3),
				new Transition('\u2776', '\u2793', state3), new Transition('\u0E50', '\u0E59', state3),
				new Transition('\u0ED0', '\u0ED9', state3), new Transition('\u1810', '\u1819', state3),
				new Transition('\u16EE', '\u16F0', state3), new Transition('\u2153', '\u2183', state3),
				new Transition('\uFF10', '\uFF19', state3), new Transition('\u2070', '\u2070', state3),
				new Transition('\u00BC', '\u00BE', state3), new Transition('\u0660', '\u0669', state3),
				new Transition('\u17E0', '\u17E9', state3), new Transition('\u3220', '\u3229', state3),
				new Transition('\u00B2', '\u00B3', state3), new Transition('\u3021', '\u3029', state3));
		state3.alter(true, 2);
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut91() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u093D', '\u093D', state1), new Transition('\u0B3D', '\u0B3D', state1),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u113E', '\u113E', state1),
				new Transition('\u01FA', '\u0217', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u0A72', '\u0A74', state1),
				new Transition('\u1140', '\u1140', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u11BA', '\u11BA', state1),
				new Transition('\u1F50', '\u1F57', state1), new Transition('\u0E87', '\u0E88', state1),
				new Transition('\u1154', '\u1155', state1), new Transition('\u0A85', '\u0A8B', state1),
				new Transition('\u0C85', '\u0C8C', state1), new Transition('\u03A3', '\u03CE', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u1FBE', '\u1FBE', state1),
				new Transition('\u04EE', '\u04F5', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u3021', '\u3029', state1), new Transition('\u11BC', '\u11C2', state1),
				new Transition('\u0A8D', '\u0A8D', state1), new Transition('\u0E8D', '\u0E8D', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u0C8E', '\u0C90', state1),
				new Transition('\u0F49', '\u0F69', state1), new Transition('\u0B5C', '\u0B5D', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u0958', '\u0961', state1),
				new Transition('\u04F8', '\u04F9', state1), new Transition('\u0B5F', '\u0B61', state1),
				new Transition('\u115F', '\u1161', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u0A2A', '\u0A30', state1), new Transition('\uAC00', '\uD7A3', state1),
				new Transition('\u0C2A', '\u0C33', state1), new Transition('\u0E94', '\u0E97', state1),
				new Transition('\u4E00', '\u9FA5', state1), new Transition('\u1163', '\u1163', state1),
				new Transition('\u0621', '\u063A', state1), new Transition('\u0E30', '\u0E30', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u1165', '\u1165', state1),
				new Transition('\u0A32', '\u0A33', state1), new Transition('\u0E32', '\u0E33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0671', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u1102', '\u1103', state1),
				new Transition('\u1169', '\u1169', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E99', '\u0E9F', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u1FD0', '\u1FD3', state1), new Transition('\u0A38', '\u0A39', state1),
				new Transition('\u0C92', '\u0CA8', state1), new Transition('\u0A93', '\u0AA8', state1),
				new Transition('\u1105', '\u1107', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u0E40', '\u0E45', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0641', '\u064A', state1),
				new Transition('\u09DF', '\u09E1', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0EB0', '\u0EB0', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u0EB2', '\u0EB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0386', '\u0386', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0388', '\u038A', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0ABD', '\u0ABD', state1), new Transition('\u0EBD', '\u0EBD', state1),
				new Transition('\u09F0', '\u09F1', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u20D0', '\u20DC', state1), new Transition('\u113E', '\u113E', state1),
				new Transition('\u01FA', '\u0217', state1), new Transition('\u0F99', '\u0FAD', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u1140', '\u1140', state1), new Transition('\u0B3C', '\u0B43', state1),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u11BA', '\u11BA', state1),
				new Transition('\u1F50', '\u1F57', state1), new Transition('\u0E87', '\u0E88', state1),
				new Transition('\u1154', '\u1155', state1), new Transition('\u0A85', '\u0A8B', state1),
				new Transition('\u09BC', '\u09BC', state1), new Transition('\u05BB', '\u05BD', state1),
				new Transition('\u0C85', '\u0C8C', state1), new Transition('\u03A3', '\u03CE', state1),
				new Transition('\u0B56', '\u0B57', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u0D57', '\u0D57', state1), new Transition('\u1FBE', '\u1FBE', state1),
				new Transition('\u04EE', '\u04F5', state1), new Transition('\u05BF', '\u05BF', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u11BC', '\u11C2', state1),
				new Transition('\u0A8D', '\u0A8D', state1), new Transition('\u0E8D', '\u0E8D', state1),
				new Transition('\u0BBE', '\u0BC2', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u05C1', '\u05C2', state1), new Transition('\u09BE', '\u09C4', state1),
				new Transition('\u06F0', '\u06F9', state1), new Transition('\u0C8E', '\u0C90', state1),
				new Transition('\u0F49', '\u0F69', state1), new Transition('\u0B5C', '\u0B5D', state1),
				new Transition('\u3021', '\u302F', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u05C4', '\u05C4', state1), new Transition('\u0958', '\u0963', state1),
				new Transition('\u04F8', '\u04F9', state1), new Transition('\u0B5F', '\u0B61', state1),
				new Transition('\u115F', '\u1161', state1), new Transition('\u0360', '\u0361', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u002D', '\u002E', state1),
				new Transition('\u0A2A', '\u0A30', state1), new Transition('\u0BC6', '\u0BC8', state1),
				new Transition('\u09C7', '\u09C8', state1), new Transition('\uAC00', '\uD7A3', state1),
				new Transition('\u0C2A', '\u0C33', state1), new Transition('\u0E94', '\u0E97', state1),
				new Transition('\u4E00', '\u9FA5', state1), new Transition('\u1163', '\u1163', state1),
				new Transition('\u0621', '\u063A', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u30FC', '\u30FE', state1), new Transition('\u1165', '\u1165', state1),
				new Transition('\u0BCA', '\u0BCD', state1), new Transition('\u09CB', '\u09CD', state1),
				new Transition('\u0A32', '\u0A33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u3099', '\u309A', state1), new Transition('\u3031', '\u3035', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0670', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0030', '\u0039', state1),
				new Transition('\u0901', '\u0903', state1), new Transition('\u0B01', '\u0B03', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u0D02', '\u0D03', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E30', '\u0E3A', state1),
				new Transition('\u0E99', '\u0E9F', state1), new Transition('\u309D', '\u309E', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u0966', '\u096F', state1),
				new Transition('\u0B66', '\u0B6F', state1), new Transition('\u0D66', '\u0D6F', state1),
				new Transition('\u1FD0', '\u1FD3', state1), new Transition('\u0A38', '\u0A39', state1),
				new Transition('\u0C92', '\u0CA8', state1), new Transition('\u0A93', '\u0AA8', state1),
				new Transition('\u1105', '\u1107', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u0A3C', '\u0A3C', state1), new Transition('\u1109', '\u1109', state1),
				new Transition('\u0B05', '\u0B0C', state1), new Transition('\u0D05', '\u0D0C', state1),
				new Transition('\u09D7', '\u09D7', state1), new Transition('\u0BD7', '\u0BD7', state1),
				new Transition('\u1F5F', '\u1F7D', state1), new Transition('\u0EA5', '\u0EA5', state1),
				new Transition('\u110B', '\u110C', state1), new Transition('\u1172', '\u1173', state1),
				new Transition('\u1FD6', '\u1FDB', state1), new Transition('\u1F00', '\u1F15', state1),
				new Transition('\u03DA', '\u03DA', state1), new Transition('\u0A3E', '\u0A42', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u0C3E', '\u0C44', state1),
				new Transition('\u1175', '\u1175', state1), new Transition('\u03DC', '\u03DC', state1),
				new Transition('\u0D0E', '\u0D10', state1), new Transition('\u0B0F', '\u0B10', state1),
				new Transition('\u09DC', '\u09DD', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0C46', '\u0C48', state1), new Transition('\u0EAD', '\u0EAE', state1),
				new Transition('\u0A47', '\u0A48', state1), new Transition('\u09DF', '\u09E3', state1),
				new Transition('\u01CD', '\u01F0', state1), new Transition('\u0E40', '\u0E4E', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0F71', '\u0F84', state1), new Transition('\u0640', '\u0652', state1),
				new Transition('\u0C4A', '\u0C4D', state1), new Transition('\u0F18', '\u0F19', state1),
				new Transition('\u0A4B', '\u0A4D', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u00F8', '\u0131', state1), new Transition('\u1FE0', '\u1FEC', state1),
				new Transition('\u2180', '\u2182', state1), new Transition('\u1F18', '\u1F1D', state1),
				new Transition('\u0981', '\u0983', state1), new Transition('\u0EB0', '\u0EB9', state1),
				new Transition('\u0B82', '\u0B83', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u00B7', '\u00B7', state1), new Transition('\u0AB5', '\u0AB9', state1),
				new Transition('\u0CB5', '\u0CB9', state1), new Transition('\u11EB', '\u11EB', state1),
				new Transition('\u0BE7', '\u0BEF', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0B13', '\u0B28', state1),
				new Transition('\u09E6', '\u09F1', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0386', '\u038A', state1), new Transition('\u0E50', '\u0E59', state1),
				new Transition('\u0C55', '\u0C56', state1), new Transition('\u0EBB', '\u0EBD', state1),
				new Transition('\u0F86', '\u0F8B', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0905', '\u0939', state1), new Transition('\u0451', '\u045C', state1),
				new Transition('\u05F0', '\u05F2', state1), new Transition('\u02BB', '\u02C1', state1),
				new Transition('\u0F20', '\u0F29', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0ABC', '\u0AC5', state1), new Transition('\u0CBE', '\u0CC4', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u0300', '\u0345', state1),
				new Transition('\u1E00', '\u1E9B', state1), new Transition('\u212A', '\u212B', state1),
				new Transition('\u0A5E', '\u0A5E', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\u11F9', '\u11F9', state1), new Transition('\u0EC6', '\u0EC6', state1),
				new Transition('\u0F90', '\u0F95', state1), new Transition('\u1FF6', '\u1FFC', state1),
				new Transition('\u0B92', '\u0B95', state1), new Transition('\u0C60', '\u0C61', state1),
				new Transition('\u0B2A', '\u0B30', state1), new Transition('\u0CC6', '\u0CC8', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u0AC7', '\u0AC9', state1), new Transition('\u06C0', '\u06CE', state1),
				new Transition('\u0F97', '\u0F97', state1), new Transition('\u0EC8', '\u0ECD', state1),
				new Transition('\u04CB', '\u04CC', state1), new Transition('\u0660', '\u0669', state1),
				new Transition('\u0CCA', '\u0CCD', state1), new Transition('\u0ACB', '\u0ACD', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut92() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0009', '\n', state0), new Transition('\u093D', '\u093D', state1),
				new Transition('\u0B3D', '\u0B3D', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u0A72', '\u0A74', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state0), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state0),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u3021', '\u3029', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u0958', '\u0961', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u0E30', '\u0E30', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u0E32', '\u0E33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0671', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u1109', '\u1109', state1),
				new Transition('\u0B05', '\u0B0C', state1), new Transition('\u0D05', '\u0D0C', state1),
				new Transition('\u1F5F', '\u1F7D', state1), new Transition('\u0EA5', '\u0EA5', state1),
				new Transition('\u110B', '\u110C', state1), new Transition('\u1172', '\u1173', state1),
				new Transition('\u1FD6', '\u1FDB', state1), new Transition('\u1F00', '\u1F15', state1),
				new Transition('\u03DA', '\u03DA', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u1175', '\u1175', state1), new Transition('\u03DC', '\u03DC', state1),
				new Transition('\u0D0E', '\u0D10', state1), new Transition('\u0B0F', '\u0B10', state1),
				new Transition('\u09DC', '\u09DD', state1), new Transition('\u0E40', '\u0E45', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0641', '\u064A', state1), new Transition('\u09DF', '\u09E1', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0EAD', '\u0EAE', state1),
				new Transition('\u01CD', '\u01F0', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0EB0', '\u0EB0', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u0EB2', '\u0EB3', state1),
				new Transition('\u00F8', '\u0131', state1), new Transition('\u1FE0', '\u1FEC', state1),
				new Transition('\u2180', '\u2182', state1), new Transition('\u1F18', '\u1F1D', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u0AB5', '\u0AB9', state1),
				new Transition('\u0CB5', '\u0CB9', state1), new Transition('\u11EB', '\u11EB', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0386', '\u0386', state1), new Transition('\u0B13', '\u0B28', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0388', '\u038A', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0ABD', '\u0ABD', state1),
				new Transition('\u0EBD', '\u0EBD', state1), new Transition('\u09F0', '\u09F1', state1),
				new Transition('\u0905', '\u0939', state1), new Transition('\u0451', '\u045C', state1),
				new Transition('\u05F0', '\u05F2', state1), new Transition('\u02BB', '\u02C1', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u1E00', '\u1E9B', state1), new Transition('\u212A', '\u212B', state1),
				new Transition('\u0A5E', '\u0A5E', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\u11F9', '\u11F9', state1), new Transition('\u1FF6', '\u1FFC', state1),
				new Transition('\u0B92', '\u0B95', state1), new Transition('\u0C60', '\u0C61', state1),
				new Transition('\u0B2A', '\u0B30', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u06C0', '\u06CE', state1),
				new Transition('\u04CB', '\u04CC', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0009', '\n', state2),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u20D0', '\u20DC', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u0F99', '\u0FAD', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state2), new Transition('\u0B3C', '\u0B43', state1),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u0020', '\u0020', state2), new Transition('\u1FB6', '\u1FBC', state1),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u09BC', '\u09BC', state1),
				new Transition('\u05BB', '\u05BD', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0B56', '\u0B57', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u0D57', '\u0D57', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u05BF', '\u05BF', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u0BBE', '\u0BC2', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u05C1', '\u05C2', state1),
				new Transition('\u09BE', '\u09C4', state1), new Transition('\u06F0', '\u06F9', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u3021', '\u302F', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u05C4', '\u05C4', state1),
				new Transition('\u0958', '\u0963', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0360', '\u0361', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u002D', '\u002E', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\u0BC6', '\u0BC8', state1), new Transition('\u09C7', '\u09C8', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0BCA', '\u0BCD', state1),
				new Transition('\u09CB', '\u09CD', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u3099', '\u309A', state1),
				new Transition('\u3031', '\u3035', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0670', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0030', '\u0039', state1), new Transition('\u0901', '\u0903', state1),
				new Transition('\u0B01', '\u0B03', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u0D02', '\u0D03', state1), new Transition('\u1102', '\u1103', state1),
				new Transition('\u1169', '\u1169', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E30', '\u0E3A', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u309D', '\u309E', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u0966', '\u096F', state1), new Transition('\u0B66', '\u0B6F', state1),
				new Transition('\u0D66', '\u0D6F', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state2.alter(true, 0, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut93() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0009', '\n', state0), new Transition('\u093D', '\u093D', state1),
				new Transition('\u0B3D', '\u0B3D', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u0A72', '\u0A74', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state0), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state0),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u3021', '\u3029', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u0958', '\u0961', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u0E30', '\u0E30', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u0E32', '\u0E33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0671', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u1109', '\u1109', state1),
				new Transition('\u0B05', '\u0B0C', state1), new Transition('\u0D05', '\u0D0C', state1),
				new Transition('\u1F5F', '\u1F7D', state1), new Transition('\u0EA5', '\u0EA5', state1),
				new Transition('\u110B', '\u110C', state1), new Transition('\u1172', '\u1173', state1),
				new Transition('\u1FD6', '\u1FDB', state1), new Transition('\u1F00', '\u1F15', state1),
				new Transition('\u03DA', '\u03DA', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u1175', '\u1175', state1), new Transition('\u03DC', '\u03DC', state1),
				new Transition('\u0D0E', '\u0D10', state1), new Transition('\u0B0F', '\u0B10', state1),
				new Transition('\u09DC', '\u09DD', state1), new Transition('\u0E40', '\u0E45', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0641', '\u064A', state1), new Transition('\u09DF', '\u09E1', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0EAD', '\u0EAE', state1),
				new Transition('\u01CD', '\u01F0', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0EB0', '\u0EB0', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u0EB2', '\u0EB3', state1),
				new Transition('\u00F8', '\u0131', state1), new Transition('\u1FE0', '\u1FEC', state1),
				new Transition('\u2180', '\u2182', state1), new Transition('\u1F18', '\u1F1D', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u0AB5', '\u0AB9', state1),
				new Transition('\u0CB5', '\u0CB9', state1), new Transition('\u11EB', '\u11EB', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0386', '\u0386', state1), new Transition('\u0B13', '\u0B28', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0388', '\u038A', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0ABD', '\u0ABD', state1),
				new Transition('\u0EBD', '\u0EBD', state1), new Transition('\u09F0', '\u09F1', state1),
				new Transition('\u0905', '\u0939', state1), new Transition('\u0451', '\u045C', state1),
				new Transition('\u05F0', '\u05F2', state1), new Transition('\u02BB', '\u02C1', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u1E00', '\u1E9B', state1), new Transition('\u212A', '\u212B', state1),
				new Transition('\u0A5E', '\u0A5E', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\u11F9', '\u11F9', state1), new Transition('\u1FF6', '\u1FFC', state1),
				new Transition('\u0B92', '\u0B95', state1), new Transition('\u0C60', '\u0C61', state1),
				new Transition('\u0B2A', '\u0B30', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u06C0', '\u06CE', state1),
				new Transition('\u04CB', '\u04CC', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0009', '\n', state2),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u20D0', '\u20DC', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u0F99', '\u0FAD', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\u0B3C', '\u0B43', state1), new Transition('\r', '\r', state2),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state2),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u09BC', '\u09BC', state1),
				new Transition('\u05BB', '\u05BD', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0B56', '\u0B57', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u0D57', '\u0D57', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u05BF', '\u05BF', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u0BBE', '\u0BC2', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u05C1', '\u05C2', state1),
				new Transition('\u09BE', '\u09C4', state1), new Transition('\u06F0', '\u06F9', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u3021', '\u302F', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u05C4', '\u05C4', state1),
				new Transition('\u0958', '\u0963', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0360', '\u0361', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u002D', '\u002E', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\u0BC6', '\u0BC8', state1), new Transition('\u09C7', '\u09C8', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0BCA', '\u0BCD', state1),
				new Transition('\u09CB', '\u09CD', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u3099', '\u309A', state1),
				new Transition('\u3031', '\u3035', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0670', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0030', '\u0039', state1), new Transition('\u0901', '\u0903', state1),
				new Transition('\u0B01', '\u0B03', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u0D02', '\u0D03', state1), new Transition('\u1102', '\u1103', state1),
				new Transition('\u1169', '\u1169', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E30', '\u0E3A', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u309D', '\u309E', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u0966', '\u096F', state1), new Transition('\u0B66', '\u0B6F', state1),
				new Transition('\u0D66', '\u0D6F', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state2.alter(true, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0009', '\n', state2), new Transition('\u093D', '\u093D', state1),
				new Transition('\u0B3D', '\u0B3D', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u0A72', '\u0A74', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state2), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state2),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u3021', '\u3029', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u0958', '\u0961', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u0E30', '\u0E30', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u0E32', '\u0E33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0671', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u1109', '\u1109', state1),
				new Transition('\u0B05', '\u0B0C', state1), new Transition('\u0D05', '\u0D0C', state1),
				new Transition('\u1F5F', '\u1F7D', state1), new Transition('\u0EA5', '\u0EA5', state1),
				new Transition('\u110B', '\u110C', state1), new Transition('\u1172', '\u1173', state1),
				new Transition('\u1FD6', '\u1FDB', state1), new Transition('\u1F00', '\u1F15', state1),
				new Transition('\u03DA', '\u03DA', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u1175', '\u1175', state1), new Transition('\u03DC', '\u03DC', state1),
				new Transition('\u0D0E', '\u0D10', state1), new Transition('\u0B0F', '\u0B10', state1),
				new Transition('\u09DC', '\u09DD', state1), new Transition('\u0E40', '\u0E45', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0641', '\u064A', state1), new Transition('\u09DF', '\u09E1', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0EAD', '\u0EAE', state1),
				new Transition('\u01CD', '\u01F0', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0EB0', '\u0EB0', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u0EB2', '\u0EB3', state1),
				new Transition('\u00F8', '\u0131', state1), new Transition('\u1FE0', '\u1FEC', state1),
				new Transition('\u2180', '\u2182', state1), new Transition('\u1F18', '\u1F1D', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u0AB5', '\u0AB9', state1),
				new Transition('\u0CB5', '\u0CB9', state1), new Transition('\u11EB', '\u11EB', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0386', '\u0386', state1), new Transition('\u0B13', '\u0B28', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0388', '\u038A', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0ABD', '\u0ABD', state1),
				new Transition('\u0EBD', '\u0EBD', state1), new Transition('\u09F0', '\u09F1', state1),
				new Transition('\u0905', '\u0939', state1), new Transition('\u0451', '\u045C', state1),
				new Transition('\u05F0', '\u05F2', state1), new Transition('\u02BB', '\u02C1', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u1E00', '\u1E9B', state1), new Transition('\u212A', '\u212B', state1),
				new Transition('\u0A5E', '\u0A5E', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\u11F9', '\u11F9', state1), new Transition('\u1FF6', '\u1FFC', state1),
				new Transition('\u0B92', '\u0B95', state1), new Transition('\u0C60', '\u0C61', state1),
				new Transition('\u0B2A', '\u0B30', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u06C0', '\u06CE', state1),
				new Transition('\u04CB', '\u04CC', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut94() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0009', '\n', state0), new Transition('\u093D', '\u093D', state1),
				new Transition('\u0B3D', '\u0B3D', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u0A72', '\u0A74', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state0), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state0),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u3021', '\u3029', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u0958', '\u0961', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u0E30', '\u0E30', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u0E32', '\u0E33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0671', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u003A', '\u003A', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u0E40', '\u0E45', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0641', '\u064A', state1),
				new Transition('\u09DF', '\u09E1', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0EB0', '\u0EB0', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u0EB2', '\u0EB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0386', '\u0386', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0388', '\u038A', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0ABD', '\u0ABD', state1), new Transition('\u0EBD', '\u0EBD', state1),
				new Transition('\u09F0', '\u09F1', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0009', '\n', state2),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u20D0', '\u20DC', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u0F99', '\u0FAD', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\u0B3C', '\u0B43', state1), new Transition('\r', '\r', state2),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state2),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u09BC', '\u09BC', state1),
				new Transition('\u05BB', '\u05BD', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0B56', '\u0B57', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u0D57', '\u0D57', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u05BF', '\u05BF', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u0BBE', '\u0BC2', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u05C1', '\u05C2', state1),
				new Transition('\u09BE', '\u09C4', state1), new Transition('\u06F0', '\u06F9', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u3021', '\u302F', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u05C4', '\u05C4', state1),
				new Transition('\u0958', '\u0963', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0360', '\u0361', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u002D', '\u002E', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\u0BC6', '\u0BC8', state1), new Transition('\u09C7', '\u09C8', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0BCA', '\u0BCD', state1),
				new Transition('\u09CB', '\u09CD', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u3099', '\u309A', state1),
				new Transition('\u3031', '\u3035', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0670', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0901', '\u0903', state1), new Transition('\u0B01', '\u0B03', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u0D02', '\u0D03', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u0030', '\u003A', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E30', '\u0E3A', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u309D', '\u309E', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u0966', '\u096F', state1), new Transition('\u0B66', '\u0B6F', state1),
				new Transition('\u0D66', '\u0D6F', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state2.alter(true, 2, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut95() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u20D0', '\u20DC', state1), new Transition('\u113E', '\u113E', state1),
				new Transition('\u01FA', '\u0217', state1), new Transition('\u0F99', '\u0FAD', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u1140', '\u1140', state1), new Transition('\u0B3C', '\u0B43', state1),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u11BA', '\u11BA', state1),
				new Transition('\u1F50', '\u1F57', state1), new Transition('\u0E87', '\u0E88', state1),
				new Transition('\u1154', '\u1155', state1), new Transition('\u0A85', '\u0A8B', state1),
				new Transition('\u09BC', '\u09BC', state1), new Transition('\u05BB', '\u05BD', state1),
				new Transition('\u0C85', '\u0C8C', state1), new Transition('\u03A3', '\u03CE', state1),
				new Transition('\u0B56', '\u0B57', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u0D57', '\u0D57', state1), new Transition('\u1FBE', '\u1FBE', state1),
				new Transition('\u04EE', '\u04F5', state1), new Transition('\u05BF', '\u05BF', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u11BC', '\u11C2', state1),
				new Transition('\u0A8D', '\u0A8D', state1), new Transition('\u0E8D', '\u0E8D', state1),
				new Transition('\u0BBE', '\u0BC2', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u05C1', '\u05C2', state1), new Transition('\u09BE', '\u09C4', state1),
				new Transition('\u06F0', '\u06F9', state1), new Transition('\u0C8E', '\u0C90', state1),
				new Transition('\u0F49', '\u0F69', state1), new Transition('\u0B5C', '\u0B5D', state1),
				new Transition('\u3021', '\u302F', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u05C4', '\u05C4', state1), new Transition('\u0958', '\u0963', state1),
				new Transition('\u04F8', '\u04F9', state1), new Transition('\u0B5F', '\u0B61', state1),
				new Transition('\u115F', '\u1161', state1), new Transition('\u0360', '\u0361', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u002D', '\u002E', state1),
				new Transition('\u0A2A', '\u0A30', state1), new Transition('\u0BC6', '\u0BC8', state1),
				new Transition('\u09C7', '\u09C8', state1), new Transition('\uAC00', '\uD7A3', state1),
				new Transition('\u0C2A', '\u0C33', state1), new Transition('\u0E94', '\u0E97', state1),
				new Transition('\u4E00', '\u9FA5', state1), new Transition('\u1163', '\u1163', state1),
				new Transition('\u0621', '\u063A', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u30FC', '\u30FE', state1), new Transition('\u1165', '\u1165', state1),
				new Transition('\u0BCA', '\u0BCD', state1), new Transition('\u09CB', '\u09CD', state1),
				new Transition('\u0A32', '\u0A33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u3099', '\u309A', state1), new Transition('\u3031', '\u3035', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0670', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0901', '\u0903', state1),
				new Transition('\u0B01', '\u0B03', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u0D02', '\u0D03', state1), new Transition('\u1102', '\u1103', state1),
				new Transition('\u1169', '\u1169', state1), new Transition('\u0030', '\u003A', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E30', '\u0E3A', state1),
				new Transition('\u0E99', '\u0E9F', state1), new Transition('\u309D', '\u309E', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u0966', '\u096F', state1),
				new Transition('\u0B66', '\u0B6F', state1), new Transition('\u0D66', '\u0D6F', state1),
				new Transition('\u1FD0', '\u1FD3', state1), new Transition('\u0A38', '\u0A39', state1),
				new Transition('\u0C92', '\u0CA8', state1), new Transition('\u0A93', '\u0AA8', state1),
				new Transition('\u1105', '\u1107', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u0A3C', '\u0A3C', state1), new Transition('\u1109', '\u1109', state1),
				new Transition('\u0B05', '\u0B0C', state1), new Transition('\u0D05', '\u0D0C', state1),
				new Transition('\u09D7', '\u09D7', state1), new Transition('\u0BD7', '\u0BD7', state1),
				new Transition('\u1F5F', '\u1F7D', state1), new Transition('\u0EA5', '\u0EA5', state1),
				new Transition('\u110B', '\u110C', state1), new Transition('\u1172', '\u1173', state1),
				new Transition('\u1FD6', '\u1FDB', state1), new Transition('\u1F00', '\u1F15', state1),
				new Transition('\u03DA', '\u03DA', state1), new Transition('\u0A3E', '\u0A42', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u0C3E', '\u0C44', state1),
				new Transition('\u1175', '\u1175', state1), new Transition('\u03DC', '\u03DC', state1),
				new Transition('\u0D0E', '\u0D10', state1), new Transition('\u0B0F', '\u0B10', state1),
				new Transition('\u09DC', '\u09DD', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0C46', '\u0C48', state1), new Transition('\u0EAD', '\u0EAE', state1),
				new Transition('\u0A47', '\u0A48', state1), new Transition('\u09DF', '\u09E3', state1),
				new Transition('\u01CD', '\u01F0', state1), new Transition('\u0E40', '\u0E4E', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0F71', '\u0F84', state1), new Transition('\u0640', '\u0652', state1),
				new Transition('\u0C4A', '\u0C4D', state1), new Transition('\u0F18', '\u0F19', state1),
				new Transition('\u0A4B', '\u0A4D', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u00F8', '\u0131', state1), new Transition('\u1FE0', '\u1FEC', state1),
				new Transition('\u2180', '\u2182', state1), new Transition('\u1F18', '\u1F1D', state1),
				new Transition('\u0981', '\u0983', state1), new Transition('\u0EB0', '\u0EB9', state1),
				new Transition('\u0B82', '\u0B83', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u00B7', '\u00B7', state1), new Transition('\u0AB5', '\u0AB9', state1),
				new Transition('\u0CB5', '\u0CB9', state1), new Transition('\u11EB', '\u11EB', state1),
				new Transition('\u0BE7', '\u0BEF', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0B13', '\u0B28', state1),
				new Transition('\u09E6', '\u09F1', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0386', '\u038A', state1), new Transition('\u0E50', '\u0E59', state1),
				new Transition('\u0C55', '\u0C56', state1), new Transition('\u0EBB', '\u0EBD', state1),
				new Transition('\u0F86', '\u0F8B', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0905', '\u0939', state1), new Transition('\u0451', '\u045C', state1),
				new Transition('\u05F0', '\u05F2', state1), new Transition('\u02BB', '\u02C1', state1),
				new Transition('\u0F20', '\u0F29', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0ABC', '\u0AC5', state1), new Transition('\u0CBE', '\u0CC4', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u0300', '\u0345', state1),
				new Transition('\u1E00', '\u1E9B', state1), new Transition('\u212A', '\u212B', state1),
				new Transition('\u0A5E', '\u0A5E', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\u11F9', '\u11F9', state1), new Transition('\u0EC6', '\u0EC6', state1),
				new Transition('\u0F90', '\u0F95', state1), new Transition('\u1FF6', '\u1FFC', state1),
				new Transition('\u0B92', '\u0B95', state1), new Transition('\u0C60', '\u0C61', state1),
				new Transition('\u0B2A', '\u0B30', state1), new Transition('\u0CC6', '\u0CC8', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u0AC7', '\u0AC9', state1), new Transition('\u06C0', '\u06CE', state1),
				new Transition('\u0F97', '\u0F97', state1), new Transition('\u0EC8', '\u0ECD', state1),
				new Transition('\u04CB', '\u04CC', state1), new Transition('\u0660', '\u0669', state1),
				new Transition('\u0CCA', '\u0CCD', state1), new Transition('\u0ACB', '\u0ACD', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut96() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0009', '\n', state0), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u093D', '\u093D', state1),
				new Transition('\u0B3D', '\u0B3D', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u0A72', '\u0A74', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state0), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u0020', '\u0020', state0), new Transition('\u1FB6', '\u1FBC', state1),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u3021', '\u3029', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u0958', '\u0961', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u0E30', '\u0E30', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u0E32', '\u0E33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0671', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u003A', '\u003A', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u0E40', '\u0E45', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0641', '\u064A', state1),
				new Transition('\u09DF', '\u09E1', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0EB0', '\u0EB0', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u0EB2', '\u0EB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0386', '\u0386', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0388', '\u038A', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0ABD', '\u0ABD', state1), new Transition('\u0EBD', '\u0EBD', state1),
				new Transition('\u09F0', '\u09F1', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0009', '\n', state2),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u20D0', '\u20DC', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u0F99', '\u0FAD', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\u0B3C', '\u0B43', state1), new Transition('\r', '\r', state2),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state2),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u09BC', '\u09BC', state1),
				new Transition('\u05BB', '\u05BD', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0B56', '\u0B57', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u0D57', '\u0D57', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u05BF', '\u05BF', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u0BBE', '\u0BC2', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u05C1', '\u05C2', state1),
				new Transition('\u09BE', '\u09C4', state1), new Transition('\u06F0', '\u06F9', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u3021', '\u302F', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u05C4', '\u05C4', state1),
				new Transition('\u0958', '\u0963', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0360', '\u0361', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u002D', '\u002E', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\u0BC6', '\u0BC8', state1), new Transition('\u09C7', '\u09C8', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0BCA', '\u0BCD', state1),
				new Transition('\u09CB', '\u09CD', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u3099', '\u309A', state1),
				new Transition('\u3031', '\u3035', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0670', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0901', '\u0903', state1), new Transition('\u0B01', '\u0B03', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u0D02', '\u0D03', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u0030', '\u003A', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E30', '\u0E3A', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u309D', '\u309E', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u0966', '\u096F', state1), new Transition('\u0B66', '\u0B6F', state1),
				new Transition('\u0D66', '\u0D6F', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state2.alter(true, 2, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0009', '\n', state2), new Transition('\u093D', '\u093D', state1),
				new Transition('\u0B3D', '\u0B3D', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u0A72', '\u0A74', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state2), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state2),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u3021', '\u3029', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u0958', '\u0961', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u0E30', '\u0E30', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u0E32', '\u0E33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0671', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0A35', '\u0A36', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u003A', '\u003A', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u0E40', '\u0E45', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0641', '\u064A', state1),
				new Transition('\u09DF', '\u09E1', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0EB0', '\u0EB0', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u0EB2', '\u0EB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0386', '\u0386', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0388', '\u038A', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0ABD', '\u0ABD', state1), new Transition('\u0EBD', '\u0EBD', state1),
				new Transition('\u09F0', '\u09F1', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut97() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDFCE', '\uDFFF', state2));
		state1.alter(false, 2, new Transition('\u1369', '\u1371', state2), new Transition('\u0030', '\u0039', state2),
				new Transition('\u06F0', '\u06F9', state2), new Transition('\u0E50', '\u0E59', state2),
				new Transition('\u0ED0', '\u0ED9', state2), new Transition('\u1810', '\u1819', state2),
				new Transition('\uD835', '\uD835', state0), new Transition('\uFF10', '\uFF19', state2),
				new Transition('\u0966', '\u096F', state2), new Transition('\u09E6', '\u09EF', state2),
				new Transition('\u0A66', '\u0A6F', state2), new Transition('\u0AE6', '\u0AEF', state2),
				new Transition('\u0B66', '\u0B6F', state2), new Transition('\u0C66', '\u0C6F', state2),
				new Transition('\u0CE6', '\u0CEF', state2), new Transition('\u0D66', '\u0D6F', state2),
				new Transition('\u0660', '\u0669', state2), new Transition('\u0BE7', '\u0BEF', state2),
				new Transition('\u0F20', '\u0F29', state2), new Transition('\u1040', '\u1049', state2),
				new Transition('\u17E0', '\u17E9', state2));
		state2.alter(true, 1);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut98() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDF4A', '\uDF4A', state2));
		state1.alter(false, 1, new Transition('\u3007', '\u3007', state2), new Transition('\uD800', '\uD800', state0),
				new Transition('\u2160', '\u2183', state2), new Transition('\u16EE', '\u16F0', state2),
				new Transition('\u3021', '\u3029', state2), new Transition('\u3038', '\u303A', state2));
		state2.alter(true, 2);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut99() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(true, 2, new Transition('\u0B99', '\u0B9A', state0), new Transition('\u00C0', '\u00D6', state0),
				new Transition('\u0591', '\u05A1', state0), new Transition('\u0F35', '\u0F35', state0),
				new Transition('\u0A02', '\u0A02', state0), new Transition('\u0C01', '\u0C03', state0),
				new Transition('\u0B9C', '\u0B9C', state0), new Transition('\u1F20', '\u1F45', state0),
				new Transition('\u02D0', '\u02D1', state0), new Transition('\u0F37', '\u0F37', state0),
				new Transition('\u119E', '\u119E', state0), new Transition('\u0B36', '\u0B39', state0),
				new Transition('\u06D0', '\u06D3', state0), new Transition('\u0B9E', '\u0B9F', state0),
				new Transition('\u0C66', '\u0C6F', state0), new Transition('\u3005', '\u3005', state0),
				new Transition('\u1F80', '\u1FB4', state0), new Transition('\u0F39', '\u0F39', state0),
				new Transition('\u0993', '\u09A8', state0), new Transition('\u0134', '\u013E', state0),
				new Transition('\u3007', '\u3007', state0), new Transition('\u0401', '\u040C', state0),
				new Transition('\u0A05', '\u0A0A', state0), new Transition('\u0A66', '\u0A74', state0),
				new Transition('\u0ED0', '\u0ED9', state0), new Transition('\u1EA0', '\u1EF9', state0),
				new Transition('\u0CD5', '\u0CD6', state0), new Transition('\u113C', '\u113C', state0),
				new Transition('\u0C05', '\u0C0C', state0), new Transition('\u0009', '\n', state2),
				new Transition('\u0061', '\u007A', state0), new Transition('\u30A1', '\u30FA', state0),
				new Transition('\u0BA3', '\u0BA4', state0), new Transition('\u20D0', '\u20DC', state0),
				new Transition('\u113E', '\u113E', state0), new Transition('\u01FA', '\u0217', state0),
				new Transition('\u0F99', '\u0FAD', state0), new Transition('\u3041', '\u3094', state0),
				new Transition('\u045E', '\u0481', state0), new Transition('\u1140', '\u1140', state0),
				new Transition('\r', '\r', state2), new Transition('\u0B3C', '\u0B43', state0),
				new Transition('\u0D3E', '\u0D43', state0), new Transition('\u11A8', '\u11A8', state0),
				new Transition('\u0180', '\u01C3', state0), new Transition('\u0C0E', '\u0C10', state0),
				new Transition('\u0A0F', '\u0A10', state0), new Transition('\u0BA8', '\u0BAA', state0),
				new Transition('\u0F3E', '\u0F47', state0), new Transition('\u0CDE', '\u0CDE', state0),
				new Transition('\u11AB', '\u11AB', state0), new Transition('\u0141', '\u0148', state0),
				new Transition('\u093C', '\u094D', state0), new Transition('\u0AE0', '\u0AE0', state0),
				new Transition('\u04D0', '\u04EB', state0), new Transition('\u06D5', '\u06E8', state0),
				new Transition('\u0CE0', '\u0CE1', state0), new Transition('\u0531', '\u0556', state0),
				new Transition('\u09AA', '\u09B0', state0), new Transition('\u0D46', '\u0D48', state0),
				new Transition('\u20E1', '\u20E1', state0), new Transition('\u0B47', '\u0B48', state0),
				new Transition('\u11AE', '\u11AF', state0), new Transition('\u05A3', '\u05B9', state0),
				new Transition('\u1F48', '\u1F4D', state0), new Transition('\u09B2', '\u09B2', state0),
				new Transition('\u0BAE', '\u0BB5', state0), new Transition('\u0D4A', '\u0D4D', state0),
				new Transition('\u114C', '\u114C', state0), new Transition('\u0B4B', '\u0B4D', state0),
				new Transition('\u10D0', '\u10F6', state0), new Transition('\u114E', '\u114E', state0),
				new Transition('\u0FB1', '\u0FB7', state0), new Transition('\u0E81', '\u0E82', state0),
				new Transition('\u0A81', '\u0A83', state0), new Transition('\u0E01', '\u0E2E', state0),
				new Transition('\u0C82', '\u0C83', state0), new Transition('\u1150', '\u1150', state0),
				new Transition('\u00D8', '\u00F6', state0), new Transition('\u0E84', '\u0E84', state0),
				new Transition('\u11B7', '\u11B8', state0), new Transition('\u09B6', '\u09B9', state0),
				new Transition('\u0250', '\u02A8', state0), new Transition('\u0483', '\u0486', state0),
				new Transition('\u0AE6', '\u0AEF', state0), new Transition('\u0BB7', '\u0BB9', state0),
				new Transition('\u0CE6', '\u0CEF', state0), new Transition('\u06EA', '\u06ED', state0),
				new Transition('\u0C12', '\u0C28', state0), new Transition('\u0FB9', '\u0FB9', state0),
				new Transition('\u0951', '\u0954', state0), new Transition('\u0A13', '\u0A28', state0),
				new Transition('\u0020', '\u0020', state2), new Transition('\u1FB6', '\u1FBC', state0),
				new Transition('\u11BA', '\u11BA', state0), new Transition('\u1F50', '\u1F57', state0),
				new Transition('\u0E87', '\u0E88', state0), new Transition('\u1154', '\u1155', state0),
				new Transition('\u0A85', '\u0A8B', state0), new Transition('\u09BC', '\u09BC', state0),
				new Transition('\u05BB', '\u05BD', state0), new Transition('\u0C85', '\u0C8C', state0),
				new Transition('\u03A3', '\u03CE', state0), new Transition('\u0B56', '\u0B57', state0),
				new Transition('\u0E8A', '\u0E8A', state0), new Transition('\u0D57', '\u0D57', state0),
				new Transition('\u1FBE', '\u1FBE', state0), new Transition('\u04EE', '\u04F5', state0),
				new Transition('\u05BF', '\u05BF', state0), new Transition('\u0559', '\u0559', state0),
				new Transition('\u1159', '\u1159', state0), new Transition('\u1F59', '\u1F59', state0),
				new Transition('\u11BC', '\u11C2', state0), new Transition('\u0A8D', '\u0A8D', state0),
				new Transition('\u0E8D', '\u0E8D', state0), new Transition('\u0BBE', '\u0BC2', state0),
				new Transition('\u1F5B', '\u1F5B', state0), new Transition('\u05C1', '\u05C2', state0),
				new Transition('\u09BE', '\u09C4', state0), new Transition('\u06F0', '\u06F9', state0),
				new Transition('\u0C8E', '\u0C90', state0), new Transition('\u0F49', '\u0F69', state0),
				new Transition('\u0B5C', '\u0B5D', state0), new Transition('\u3021', '\u302F', state0),
				new Transition('\u1FC2', '\u1FC4', state0), new Transition('\u0A8F', '\u0A91', state0),
				new Transition('\u1F5D', '\u1F5D', state0), new Transition('\u05C4', '\u05C4', state0),
				new Transition('\u0958', '\u0963', state0), new Transition('\u04F8', '\u04F9', state0),
				new Transition('\u0B5F', '\u0B61', state0), new Transition('\u115F', '\u1161', state0),
				new Transition('\u0360', '\u0361', state0), new Transition('\u0D60', '\u0D61', state0),
				new Transition('\u002D', '\u002E', state0), new Transition('\u0A2A', '\u0A30', state0),
				new Transition('\u0BC6', '\u0BC8', state0), new Transition('\u09C7', '\u09C8', state0),
				new Transition('\uAC00', '\uD7A3', state0), new Transition('\u0C2A', '\u0C33', state0),
				new Transition('\u0E94', '\u0E97', state0), new Transition('\u4E00', '\u9FA5', state0),
				new Transition('\u1163', '\u1163', state0), new Transition('\u0621', '\u063A', state0),
				new Transition('\u1FC6', '\u1FCC', state0), new Transition('\u30FC', '\u30FE', state0),
				new Transition('\u1165', '\u1165', state0), new Transition('\u0BCA', '\u0BCD', state0),
				new Transition('\u09CB', '\u09CD', state0), new Transition('\u0A32', '\u0A33', state0),
				new Transition('\u1100', '\u1100', state0), new Transition('\u3099', '\u309A', state0),
				new Transition('\u3031', '\u3035', state0), new Transition('\u1167', '\u1167', state0),
				new Transition('\u0670', '\u06B7', state0), new Transition('\u040E', '\u044F', state0),
				new Transition('\u0901', '\u0903', state0), new Transition('\u0B01', '\u0B03', state0),
				new Transition('\u0A35', '\u0A36', state0), new Transition('\u0D02', '\u0D03', state0),
				new Transition('\u1102', '\u1103', state0), new Transition('\u1169', '\u1169', state0),
				new Transition('\u0030', '\u003A', state0), new Transition('\u014A', '\u017E', state0),
				new Transition('\u0E30', '\u0E3A', state0), new Transition('\u0E99', '\u0E9F', state0),
				new Transition('\u309D', '\u309E', state0), new Transition('\u0C35', '\u0C39', state0),
				new Transition('\u0966', '\u096F', state0), new Transition('\u0B66', '\u0B6F', state0),
				new Transition('\u0D66', '\u0D6F', state0), new Transition('\u1FD0', '\u1FD3', state0),
				new Transition('\u0A38', '\u0A39', state0), new Transition('\u0C92', '\u0CA8', state0),
				new Transition('\u0A93', '\u0AA8', state0), new Transition('\u1105', '\u1107', state0),
				new Transition('\u03D0', '\u03D6', state0), new Transition('\u116D', '\u116E', state0),
				new Transition('\u0EA1', '\u0EA3', state0), new Transition('\u0A3C', '\u0A3C', state0),
				new Transition('\u1109', '\u1109', state0), new Transition('\u0B05', '\u0B0C', state0),
				new Transition('\u0D05', '\u0D0C', state0), new Transition('\u09D7', '\u09D7', state0),
				new Transition('\u0BD7', '\u0BD7', state0), new Transition('\u1F5F', '\u1F7D', state0),
				new Transition('\u0EA5', '\u0EA5', state0), new Transition('\u110B', '\u110C', state0),
				new Transition('\u1172', '\u1173', state0), new Transition('\u1FD6', '\u1FDB', state0),
				new Transition('\u1F00', '\u1F15', state0), new Transition('\u03DA', '\u03DA', state0),
				new Transition('\u0A3E', '\u0A42', state0), new Transition('\u0EA7', '\u0EA7', state0),
				new Transition('\u0C3E', '\u0C44', state0), new Transition('\u1175', '\u1175', state0),
				new Transition('\u03DC', '\u03DC', state0), new Transition('\u0D0E', '\u0D10', state0),
				new Transition('\u0B0F', '\u0B10', state0), new Transition('\u09DC', '\u09DD', state0),
				new Transition('\u110E', '\u1112', state0), new Transition('\u0561', '\u0586', state0),
				new Transition('\u0EAA', '\u0EAB', state0), new Transition('\u03DE', '\u03DE', state0),
				new Transition('\u05D0', '\u05EA', state0), new Transition('\u03E0', '\u03E0', state0),
				new Transition('\u0AAA', '\u0AB0', state0), new Transition('\u0C46', '\u0C48', state0),
				new Transition('\u0EAD', '\u0EAE', state0), new Transition('\u0A47', '\u0A48', state0),
				new Transition('\u09DF', '\u09E3', state0), new Transition('\u01CD', '\u01F0', state0),
				new Transition('\u0E40', '\u0E4E', state0), new Transition('\u0490', '\u04C4', state0),
				new Transition('\u0CAA', '\u0CB3', state0), new Transition('\u0F71', '\u0F84', state0),
				new Transition('\u0640', '\u0652', state0), new Transition('\u0C4A', '\u0C4D', state0),
				new Transition('\u0F18', '\u0F19', state0), new Transition('\u0A4B', '\u0A4D', state0),
				new Transition('\u0AB2', '\u0AB3', state0), new Transition('\u00F8', '\u0131', state0),
				new Transition('\u1FE0', '\u1FEC', state0), new Transition('\u2180', '\u2182', state0),
				new Transition('\u1F18', '\u1F1D', state0), new Transition('\u0981', '\u0983', state0),
				new Transition('\u0EB0', '\u0EB9', state0), new Transition('\u0B82', '\u0B83', state0),
				new Transition('\u3105', '\u312C', state0), new Transition('\u10A0', '\u10C5', state0),
				new Transition('\u0041', '\u005A', state0), new Transition('\u00B7', '\u00B7', state0),
				new Transition('\u0AB5', '\u0AB9', state0), new Transition('\u0CB5', '\u0CB9', state0),
				new Transition('\u11EB', '\u11EB', state0), new Transition('\u0BE7', '\u0BEF', state0),
				new Transition('\u0D12', '\u0D28', state0), new Transition('\u03E2', '\u03F3', state0),
				new Transition('\u0B13', '\u0B28', state0), new Transition('\u09E6', '\u09F1', state0),
				new Transition('\u0B85', '\u0B8A', state0), new Transition('\u0386', '\u038A', state0),
				new Transition('\u0E50', '\u0E59', state0), new Transition('\u0C55', '\u0C56', state0),
				new Transition('\u0EBB', '\u0EBD', state0), new Transition('\u0F86', '\u0F8B', state0),
				new Transition('\u06BA', '\u06BE', state0), new Transition('\u0985', '\u098C', state0),
				new Transition('\u11F0', '\u11F0', state0), new Transition('\u0905', '\u0939', state0),
				new Transition('\u0451', '\u045C', state0), new Transition('\u05F0', '\u05F2', state0),
				new Transition('\u02BB', '\u02C1', state0), new Transition('\u0F20', '\u0F29', state0),
				new Transition('\u038C', '\u038C', state0), new Transition('\u2126', '\u2126', state0),
				new Transition('\u1FF2', '\u1FF4', state0), new Transition('\u0A59', '\u0A5C', state0),
				new Transition('\u01F4', '\u01F5', state0), new Transition('\u0ABC', '\u0AC5', state0),
				new Transition('\u0CBE', '\u0CC4', state0), new Transition('\u0B8E', '\u0B90', state0),
				new Transition('\u0EC0', '\u0EC4', state0), new Transition('\u098F', '\u0990', state0),
				new Transition('\u0300', '\u0345', state0), new Transition('\u1E00', '\u1E9B', state0),
				new Transition('\u212A', '\u212B', state0), new Transition('\u0A5E', '\u0A5E', state0),
				new Transition('\u005F', '\u005F', state0), new Transition('\u11F9', '\u11F9', state0),
				new Transition('\u0EC6', '\u0EC6', state0), new Transition('\u0F90', '\u0F95', state0),
				new Transition('\u1FF6', '\u1FFC', state0), new Transition('\u0B92', '\u0B95', state0),
				new Transition('\u0C60', '\u0C61', state0), new Transition('\u0B2A', '\u0B30', state0),
				new Transition('\u0CC6', '\u0CC8', state0), new Transition('\u04C7', '\u04C8', state0),
				new Transition('\u212E', '\u212E', state0), new Transition('\u0AC7', '\u0AC9', state0),
				new Transition('\u06C0', '\u06CE', state0), new Transition('\u0F97', '\u0F97', state0),
				new Transition('\u0EC8', '\u0ECD', state0), new Transition('\u04CB', '\u04CC', state0),
				new Transition('\u0660', '\u0669', state0), new Transition('\u0CCA', '\u0CCD', state0),
				new Transition('\u0ACB', '\u0ACD', state0), new Transition('\u0B32', '\u0B33', state0),
				new Transition('\u038E', '\u03A1', state0), new Transition('\u0D2A', '\u0D39', state0));
		state1.alter(false, 1, new Transition('\u0B99', '\u0B9A', state0), new Transition('\u00C0', '\u00D6', state0),
				new Transition('\u0591', '\u05A1', state0), new Transition('\u0F35', '\u0F35', state0),
				new Transition('\u0A02', '\u0A02', state0), new Transition('\u0C01', '\u0C03', state0),
				new Transition('\u0B9C', '\u0B9C', state0), new Transition('\u1F20', '\u1F45', state0),
				new Transition('\u02D0', '\u02D1', state0), new Transition('\u0F37', '\u0F37', state0),
				new Transition('\u119E', '\u119E', state0), new Transition('\u0B36', '\u0B39', state0),
				new Transition('\u06D0', '\u06D3', state0), new Transition('\u0B9E', '\u0B9F', state0),
				new Transition('\u0C66', '\u0C6F', state0), new Transition('\u3005', '\u3005', state0),
				new Transition('\u1F80', '\u1FB4', state0), new Transition('\u0F39', '\u0F39', state0),
				new Transition('\u0993', '\u09A8', state0), new Transition('\u0134', '\u013E', state0),
				new Transition('\u3007', '\u3007', state0), new Transition('\u0401', '\u040C', state0),
				new Transition('\u0A05', '\u0A0A', state0), new Transition('\u0A66', '\u0A74', state0),
				new Transition('\u0ED0', '\u0ED9', state0), new Transition('\u1EA0', '\u1EF9', state0),
				new Transition('\u0CD5', '\u0CD6', state0), new Transition('\u113C', '\u113C', state0),
				new Transition('\u0C05', '\u0C0C', state0), new Transition('\u0009', '\n', state1),
				new Transition('\u0061', '\u007A', state0), new Transition('\u30A1', '\u30FA', state0),
				new Transition('\u0BA3', '\u0BA4', state0), new Transition('\u20D0', '\u20DC', state0),
				new Transition('\u113E', '\u113E', state0), new Transition('\u01FA', '\u0217', state0),
				new Transition('\u0F99', '\u0FAD', state0), new Transition('\u3041', '\u3094', state0),
				new Transition('\u045E', '\u0481', state0), new Transition('\u1140', '\u1140', state0),
				new Transition('\r', '\r', state1), new Transition('\u0B3C', '\u0B43', state0),
				new Transition('\u0D3E', '\u0D43', state0), new Transition('\u11A8', '\u11A8', state0),
				new Transition('\u0180', '\u01C3', state0), new Transition('\u0C0E', '\u0C10', state0),
				new Transition('\u0A0F', '\u0A10', state0), new Transition('\u0BA8', '\u0BAA', state0),
				new Transition('\u0F3E', '\u0F47', state0), new Transition('\u0CDE', '\u0CDE', state0),
				new Transition('\u11AB', '\u11AB', state0), new Transition('\u0141', '\u0148', state0),
				new Transition('\u093C', '\u094D', state0), new Transition('\u0AE0', '\u0AE0', state0),
				new Transition('\u04D0', '\u04EB', state0), new Transition('\u06D5', '\u06E8', state0),
				new Transition('\u0CE0', '\u0CE1', state0), new Transition('\u0531', '\u0556', state0),
				new Transition('\u09AA', '\u09B0', state0), new Transition('\u0D46', '\u0D48', state0),
				new Transition('\u20E1', '\u20E1', state0), new Transition('\u0B47', '\u0B48', state0),
				new Transition('\u11AE', '\u11AF', state0), new Transition('\u05A3', '\u05B9', state0),
				new Transition('\u1F48', '\u1F4D', state0), new Transition('\u09B2', '\u09B2', state0),
				new Transition('\u0BAE', '\u0BB5', state0), new Transition('\u0D4A', '\u0D4D', state0),
				new Transition('\u114C', '\u114C', state0), new Transition('\u0B4B', '\u0B4D', state0),
				new Transition('\u10D0', '\u10F6', state0), new Transition('\u114E', '\u114E', state0),
				new Transition('\u0FB1', '\u0FB7', state0), new Transition('\u0E81', '\u0E82', state0),
				new Transition('\u0A81', '\u0A83', state0), new Transition('\u0E01', '\u0E2E', state0),
				new Transition('\u0C82', '\u0C83', state0), new Transition('\u1150', '\u1150', state0),
				new Transition('\u00D8', '\u00F6', state0), new Transition('\u0E84', '\u0E84', state0),
				new Transition('\u11B7', '\u11B8', state0), new Transition('\u09B6', '\u09B9', state0),
				new Transition('\u0250', '\u02A8', state0), new Transition('\u0483', '\u0486', state0),
				new Transition('\u0AE6', '\u0AEF', state0), new Transition('\u0BB7', '\u0BB9', state0),
				new Transition('\u0CE6', '\u0CEF', state0), new Transition('\u06EA', '\u06ED', state0),
				new Transition('\u0C12', '\u0C28', state0), new Transition('\u0FB9', '\u0FB9', state0),
				new Transition('\u0951', '\u0954', state0), new Transition('\u0A13', '\u0A28', state0),
				new Transition('\u0020', '\u0020', state1), new Transition('\u1FB6', '\u1FBC', state0),
				new Transition('\u11BA', '\u11BA', state0), new Transition('\u1F50', '\u1F57', state0),
				new Transition('\u0E87', '\u0E88', state0), new Transition('\u1154', '\u1155', state0),
				new Transition('\u0A85', '\u0A8B', state0), new Transition('\u09BC', '\u09BC', state0),
				new Transition('\u05BB', '\u05BD', state0), new Transition('\u0C85', '\u0C8C', state0),
				new Transition('\u03A3', '\u03CE', state0), new Transition('\u0B56', '\u0B57', state0),
				new Transition('\u0E8A', '\u0E8A', state0), new Transition('\u0D57', '\u0D57', state0),
				new Transition('\u1FBE', '\u1FBE', state0), new Transition('\u04EE', '\u04F5', state0),
				new Transition('\u05BF', '\u05BF', state0), new Transition('\u0559', '\u0559', state0),
				new Transition('\u1159', '\u1159', state0), new Transition('\u1F59', '\u1F59', state0),
				new Transition('\u11BC', '\u11C2', state0), new Transition('\u0A8D', '\u0A8D', state0),
				new Transition('\u0E8D', '\u0E8D', state0), new Transition('\u0BBE', '\u0BC2', state0),
				new Transition('\u1F5B', '\u1F5B', state0), new Transition('\u05C1', '\u05C2', state0),
				new Transition('\u09BE', '\u09C4', state0), new Transition('\u06F0', '\u06F9', state0),
				new Transition('\u0C8E', '\u0C90', state0), new Transition('\u0F49', '\u0F69', state0),
				new Transition('\u0B5C', '\u0B5D', state0), new Transition('\u3021', '\u302F', state0),
				new Transition('\u1FC2', '\u1FC4', state0), new Transition('\u0A8F', '\u0A91', state0),
				new Transition('\u1F5D', '\u1F5D', state0), new Transition('\u05C4', '\u05C4', state0),
				new Transition('\u0958', '\u0963', state0), new Transition('\u04F8', '\u04F9', state0),
				new Transition('\u0B5F', '\u0B61', state0), new Transition('\u115F', '\u1161', state0),
				new Transition('\u0360', '\u0361', state0), new Transition('\u0D60', '\u0D61', state0),
				new Transition('\u002D', '\u002E', state0), new Transition('\u0A2A', '\u0A30', state0),
				new Transition('\u0BC6', '\u0BC8', state0), new Transition('\u09C7', '\u09C8', state0),
				new Transition('\uAC00', '\uD7A3', state0), new Transition('\u0C2A', '\u0C33', state0),
				new Transition('\u0E94', '\u0E97', state0), new Transition('\u4E00', '\u9FA5', state0),
				new Transition('\u1163', '\u1163', state0), new Transition('\u0621', '\u063A', state0),
				new Transition('\u1FC6', '\u1FCC', state0), new Transition('\u30FC', '\u30FE', state0),
				new Transition('\u1165', '\u1165', state0), new Transition('\u0BCA', '\u0BCD', state0),
				new Transition('\u09CB', '\u09CD', state0), new Transition('\u0A32', '\u0A33', state0),
				new Transition('\u1100', '\u1100', state0), new Transition('\u3099', '\u309A', state0),
				new Transition('\u3031', '\u3035', state0), new Transition('\u1167', '\u1167', state0),
				new Transition('\u0670', '\u06B7', state0), new Transition('\u040E', '\u044F', state0),
				new Transition('\u0901', '\u0903', state0), new Transition('\u0B01', '\u0B03', state0),
				new Transition('\u0A35', '\u0A36', state0), new Transition('\u0D02', '\u0D03', state0),
				new Transition('\u1102', '\u1103', state0), new Transition('\u1169', '\u1169', state0),
				new Transition('\u0030', '\u003A', state0), new Transition('\u014A', '\u017E', state0),
				new Transition('\u0E30', '\u0E3A', state0), new Transition('\u0E99', '\u0E9F', state0),
				new Transition('\u309D', '\u309E', state0), new Transition('\u0C35', '\u0C39', state0),
				new Transition('\u0966', '\u096F', state0), new Transition('\u0B66', '\u0B6F', state0),
				new Transition('\u0D66', '\u0D6F', state0), new Transition('\u1FD0', '\u1FD3', state0),
				new Transition('\u0A38', '\u0A39', state0), new Transition('\u0C92', '\u0CA8', state0),
				new Transition('\u0A93', '\u0AA8', state0), new Transition('\u1105', '\u1107', state0),
				new Transition('\u03D0', '\u03D6', state0), new Transition('\u116D', '\u116E', state0),
				new Transition('\u0EA1', '\u0EA3', state0), new Transition('\u0A3C', '\u0A3C', state0),
				new Transition('\u1109', '\u1109', state0), new Transition('\u0B05', '\u0B0C', state0),
				new Transition('\u0D05', '\u0D0C', state0), new Transition('\u09D7', '\u09D7', state0),
				new Transition('\u0BD7', '\u0BD7', state0), new Transition('\u1F5F', '\u1F7D', state0),
				new Transition('\u0EA5', '\u0EA5', state0), new Transition('\u110B', '\u110C', state0),
				new Transition('\u1172', '\u1173', state0), new Transition('\u1FD6', '\u1FDB', state0),
				new Transition('\u1F00', '\u1F15', state0), new Transition('\u03DA', '\u03DA', state0),
				new Transition('\u0A3E', '\u0A42', state0), new Transition('\u0EA7', '\u0EA7', state0),
				new Transition('\u0C3E', '\u0C44', state0), new Transition('\u1175', '\u1175', state0),
				new Transition('\u03DC', '\u03DC', state0), new Transition('\u0D0E', '\u0D10', state0),
				new Transition('\u0B0F', '\u0B10', state0), new Transition('\u09DC', '\u09DD', state0),
				new Transition('\u110E', '\u1112', state0), new Transition('\u0561', '\u0586', state0),
				new Transition('\u0EAA', '\u0EAB', state0), new Transition('\u03DE', '\u03DE', state0),
				new Transition('\u05D0', '\u05EA', state0), new Transition('\u03E0', '\u03E0', state0),
				new Transition('\u0AAA', '\u0AB0', state0), new Transition('\u0C46', '\u0C48', state0),
				new Transition('\u0EAD', '\u0EAE', state0), new Transition('\u0A47', '\u0A48', state0),
				new Transition('\u09DF', '\u09E3', state0), new Transition('\u01CD', '\u01F0', state0),
				new Transition('\u0E40', '\u0E4E', state0), new Transition('\u0490', '\u04C4', state0),
				new Transition('\u0CAA', '\u0CB3', state0), new Transition('\u0F71', '\u0F84', state0),
				new Transition('\u0640', '\u0652', state0), new Transition('\u0C4A', '\u0C4D', state0),
				new Transition('\u0F18', '\u0F19', state0), new Transition('\u0A4B', '\u0A4D', state0),
				new Transition('\u0AB2', '\u0AB3', state0), new Transition('\u00F8', '\u0131', state0),
				new Transition('\u1FE0', '\u1FEC', state0), new Transition('\u2180', '\u2182', state0),
				new Transition('\u1F18', '\u1F1D', state0), new Transition('\u0981', '\u0983', state0),
				new Transition('\u0EB0', '\u0EB9', state0), new Transition('\u0B82', '\u0B83', state0),
				new Transition('\u3105', '\u312C', state0), new Transition('\u10A0', '\u10C5', state0),
				new Transition('\u0041', '\u005A', state0), new Transition('\u00B7', '\u00B7', state0),
				new Transition('\u0AB5', '\u0AB9', state0), new Transition('\u0CB5', '\u0CB9', state0),
				new Transition('\u11EB', '\u11EB', state0), new Transition('\u0BE7', '\u0BEF', state0),
				new Transition('\u0D12', '\u0D28', state0), new Transition('\u03E2', '\u03F3', state0),
				new Transition('\u0B13', '\u0B28', state0), new Transition('\u09E6', '\u09F1', state0),
				new Transition('\u0B85', '\u0B8A', state0), new Transition('\u0386', '\u038A', state0),
				new Transition('\u0E50', '\u0E59', state0), new Transition('\u0C55', '\u0C56', state0),
				new Transition('\u0EBB', '\u0EBD', state0), new Transition('\u0F86', '\u0F8B', state0),
				new Transition('\u06BA', '\u06BE', state0), new Transition('\u0985', '\u098C', state0),
				new Transition('\u11F0', '\u11F0', state0), new Transition('\u0905', '\u0939', state0),
				new Transition('\u0451', '\u045C', state0), new Transition('\u05F0', '\u05F2', state0),
				new Transition('\u02BB', '\u02C1', state0), new Transition('\u0F20', '\u0F29', state0),
				new Transition('\u038C', '\u038C', state0), new Transition('\u2126', '\u2126', state0),
				new Transition('\u1FF2', '\u1FF4', state0), new Transition('\u0A59', '\u0A5C', state0),
				new Transition('\u01F4', '\u01F5', state0), new Transition('\u0ABC', '\u0AC5', state0),
				new Transition('\u0CBE', '\u0CC4', state0), new Transition('\u0B8E', '\u0B90', state0),
				new Transition('\u0EC0', '\u0EC4', state0), new Transition('\u098F', '\u0990', state0),
				new Transition('\u0300', '\u0345', state0), new Transition('\u1E00', '\u1E9B', state0),
				new Transition('\u212A', '\u212B', state0), new Transition('\u0A5E', '\u0A5E', state0),
				new Transition('\u005F', '\u005F', state0), new Transition('\u11F9', '\u11F9', state0),
				new Transition('\u0EC6', '\u0EC6', state0), new Transition('\u0F90', '\u0F95', state0),
				new Transition('\u1FF6', '\u1FFC', state0), new Transition('\u0B92', '\u0B95', state0),
				new Transition('\u0C60', '\u0C61', state0), new Transition('\u0B2A', '\u0B30', state0),
				new Transition('\u0CC6', '\u0CC8', state0), new Transition('\u04C7', '\u04C8', state0),
				new Transition('\u212E', '\u212E', state0), new Transition('\u0AC7', '\u0AC9', state0),
				new Transition('\u06C0', '\u06CE', state0), new Transition('\u0F97', '\u0F97', state0),
				new Transition('\u0EC8', '\u0ECD', state0), new Transition('\u04CB', '\u04CC', state0),
				new Transition('\u0660', '\u0669', state0), new Transition('\u0CCA', '\u0CCD', state0),
				new Transition('\u0ACB', '\u0ACD', state0), new Transition('\u0B32', '\u0B33', state0),
				new Transition('\u038E', '\u03A1', state0), new Transition('\u0D2A', '\u0D39', state0));
		state2.alter(true, 0, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut100() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0009', '\n', state0),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u20D0', '\u20DC', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u0F99', '\u0FAD', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\u0B3C', '\u0B43', state1), new Transition('\r', '\r', state0),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u0020', '\u0020', state0),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u09BC', '\u09BC', state1),
				new Transition('\u05BB', '\u05BD', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0B56', '\u0B57', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u0D57', '\u0D57', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u05BF', '\u05BF', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u0BBE', '\u0BC2', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u05C1', '\u05C2', state1),
				new Transition('\u09BE', '\u09C4', state1), new Transition('\u06F0', '\u06F9', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u3021', '\u302F', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u05C4', '\u05C4', state1),
				new Transition('\u0958', '\u0963', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0360', '\u0361', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u002D', '\u002E', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\u0BC6', '\u0BC8', state1), new Transition('\u09C7', '\u09C8', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0BCA', '\u0BCD', state1),
				new Transition('\u09CB', '\u09CD', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u3099', '\u309A', state1),
				new Transition('\u3031', '\u3035', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0670', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0901', '\u0903', state1), new Transition('\u0B01', '\u0B03', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u0D02', '\u0D03', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u0030', '\u003A', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E30', '\u0E3A', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u309D', '\u309E', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u0966', '\u096F', state1), new Transition('\u0B66', '\u0B6F', state1),
				new Transition('\u0D66', '\u0D6F', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state1.alter(true, 0, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0009', '\n', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u20D0', '\u20DC', state1),
				new Transition('\u113E', '\u113E', state1), new Transition('\u01FA', '\u0217', state1),
				new Transition('\u0F99', '\u0FAD', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u1140', '\u1140', state1),
				new Transition('\r', '\r', state1), new Transition('\u0B3C', '\u0B43', state1),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u0020', '\u0020', state1), new Transition('\u1FB6', '\u1FBC', state1),
				new Transition('\u11BA', '\u11BA', state1), new Transition('\u1F50', '\u1F57', state1),
				new Transition('\u0E87', '\u0E88', state1), new Transition('\u1154', '\u1155', state1),
				new Transition('\u0A85', '\u0A8B', state1), new Transition('\u09BC', '\u09BC', state1),
				new Transition('\u05BB', '\u05BD', state1), new Transition('\u0C85', '\u0C8C', state1),
				new Transition('\u03A3', '\u03CE', state1), new Transition('\u0B56', '\u0B57', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u0D57', '\u0D57', state1),
				new Transition('\u1FBE', '\u1FBE', state1), new Transition('\u04EE', '\u04F5', state1),
				new Transition('\u05BF', '\u05BF', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u11BC', '\u11C2', state1), new Transition('\u0A8D', '\u0A8D', state1),
				new Transition('\u0E8D', '\u0E8D', state1), new Transition('\u0BBE', '\u0BC2', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u05C1', '\u05C2', state1),
				new Transition('\u09BE', '\u09C4', state1), new Transition('\u06F0', '\u06F9', state1),
				new Transition('\u0C8E', '\u0C90', state1), new Transition('\u0F49', '\u0F69', state1),
				new Transition('\u0B5C', '\u0B5D', state1), new Transition('\u3021', '\u302F', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u05C4', '\u05C4', state1),
				new Transition('\u0958', '\u0963', state1), new Transition('\u04F8', '\u04F9', state1),
				new Transition('\u0B5F', '\u0B61', state1), new Transition('\u115F', '\u1161', state1),
				new Transition('\u0360', '\u0361', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u002D', '\u002E', state1), new Transition('\u0A2A', '\u0A30', state1),
				new Transition('\u0BC6', '\u0BC8', state1), new Transition('\u09C7', '\u09C8', state1),
				new Transition('\uAC00', '\uD7A3', state1), new Transition('\u0C2A', '\u0C33', state1),
				new Transition('\u0E94', '\u0E97', state1), new Transition('\u4E00', '\u9FA5', state1),
				new Transition('\u1163', '\u1163', state1), new Transition('\u0621', '\u063A', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u30FC', '\u30FE', state1),
				new Transition('\u1165', '\u1165', state1), new Transition('\u0BCA', '\u0BCD', state1),
				new Transition('\u09CB', '\u09CD', state1), new Transition('\u0A32', '\u0A33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u3099', '\u309A', state1),
				new Transition('\u3031', '\u3035', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0670', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0901', '\u0903', state1), new Transition('\u0B01', '\u0B03', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u0D02', '\u0D03', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u0030', '\u003A', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E30', '\u0E3A', state1), new Transition('\u0E99', '\u0E9F', state1),
				new Transition('\u309D', '\u309E', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u0966', '\u096F', state1), new Transition('\u0B66', '\u0B6F', state1),
				new Transition('\u0D66', '\u0D6F', state1), new Transition('\u1FD0', '\u1FD3', state1),
				new Transition('\u0A38', '\u0A39', state1), new Transition('\u0C92', '\u0CA8', state1),
				new Transition('\u0A93', '\u0AA8', state1), new Transition('\u1105', '\u1107', state1),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut101() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\uDF20', '\uDF23', state2));
		state1.alter(false, 1, new Transition('\u2153', '\u215F', state2), new Transition('\u3192', '\u3195', state2),
				new Transition('\uD800', '\uD800', state0), new Transition('\u2776', '\u2793', state2),
				new Transition('\u0F2A', '\u0F33', state2), new Transition('\u2070', '\u2070', state2),
				new Transition('\u2460', '\u249B', state2), new Transition('\u00BC', '\u00BE', state2),
				new Transition('\u24EA', '\u24EA', state2), new Transition('\u09F4', '\u09F9', state2),
				new Transition('\u2074', '\u2079', state2), new Transition('\u0BF0', '\u0BF2', state2),
				new Transition('\u1372', '\u137C', state2), new Transition('\u2080', '\u2089', state2),
				new Transition('\u3220', '\u3229', state2), new Transition('\u3280', '\u3289', state2),
				new Transition('\u00B2', '\u00B3', state2), new Transition('\u00B9', '\u00B9', state2));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut102() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2150', '\u218F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut103() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1680', '\u169F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut104() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDF00', '\uDF2F', state2));
		state1.alter(false, 0, new Transition('\uD800', '\uD800', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut105() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2440', '\u245F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut106() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0B00', '\u0B7F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut107() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u104A', '\u104F', state1), new Transition('\uFF1A', '\uFF1B', state1),
				new Transition('\u3014', '\u301F', state1), new Transition('\u061B', '\u061B', state1),
				new Transition('\u169B', '\u169C', state1), new Transition('\u0E4F', '\u0E4F', state1),
				new Transition('\u3001', '\u3003', state1), new Transition('\uFE49', '\uFE52', state1),
				new Transition('\uFE68', '\uFE68', state1), new Transition('\uFF01', '\uFF03', state1),
				new Transition('\uFE6A', '\uFE6B', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u2010', '\u2027', state1), new Transition('\u0F85', '\u0F85', state1),
				new Transition('\uFF1F', '\uFF20', state1), new Transition('\u061F', '\u061F', state1),
				new Transition('\u066A', '\u066D', state1), new Transition('\u16EB', '\u16ED', state1),
				new Transition('\u1800', '\u180A', state1), new Transition('\u0387', '\u0387', state1),
				new Transition('\u06D4', '\u06D4', state1), new Transition('\u166D', '\u166E', state1),
				new Transition('\u003A', '\u003B', state1), new Transition('\u00A1', '\u00A1', state1),
				new Transition('\u00BB', '\u00BB', state1), new Transition('\u0700', '\u070D', state1),
				new Transition('\uFE30', '\uFE44', state1), new Transition('\u2030', '\u2043', state1),
				new Transition('\uFF3B', '\uFF3D', state1), new Transition('\u0021', '\u0023', state1),
				new Transition('\u0F3A', '\u0F3D', state1), new Transition('\uFF05', '\uFF0A', state1),
				new Transition('\u0589', '\u058A', state1), new Transition('\u0970', '\u0970', state1),
				new Transition('\u05BE', '\u05BE', state1), new Transition('\u17D4', '\u17DA', state1),
				new Transition('\u00BF', '\u00BF', state1), new Transition('\u060C', '\u060C', state1),
				new Transition('\uFD3E', '\uFD3F', state1), new Transition('\u003F', '\u0040', state1),
				new Transition('\u0F04', '\u0F12', state1), new Transition('\uFF3F', '\uFF3F', state1),
				new Transition('\u05C0', '\u05C0', state1), new Transition('\uFF0C', '\uFF0F', state1),
				new Transition('\u05F3', '\u05F4', state1), new Transition('\u3008', '\u3011', state1),
				new Transition('\uFF5B', '\uFF5B', state1), new Transition('\u0DF4', '\u0DF4', state1),
				new Transition('\u208D', '\u208E', state1), new Transition('\u0E5A', '\u0E5B', state1),
				new Transition('\u0025', '\u002A', state1), new Transition('\u17DC', '\u17DC', state1),
				new Transition('\u005B', '\u005D', state1), new Transition('\u05C3', '\u05C3', state1),
				new Transition('\uFE54', '\uFE61', state1), new Transition('\u2329', '\u232A', state1),
				new Transition('\u055A', '\u055F', state1), new Transition('\uFF5D', '\uFF5D', state1),
				new Transition('\u00AB', '\u00AB', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\u2045', '\u2046', state1), new Transition('\u00AD', '\u00AD', state1),
				new Transition('\u002C', '\u002F', state1), new Transition('\u007B', '\u007B', state1),
				new Transition('\u10FB', '\u10FB', state1), new Transition('\u30FB', '\u30FB', state1),
				new Transition('\uFE63', '\uFE63', state1), new Transition('\u3030', '\u3030', state1),
				new Transition('\u007D', '\u007D', state1), new Transition('\u207D', '\u207E', state1),
				new Transition('\uFF61', '\uFF65', state1), new Transition('\u037E', '\u037E', state1),
				new Transition('\u0964', '\u0965', state1), new Transition('\u2048', '\u204D', state1),
				new Transition('\u1361', '\u1368', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut108() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\uFE4D', '\uFE4F', state1), new Transition('\uFE33', '\uFE34', state1),
				new Transition('\u30FB', '\u30FB', state1), new Transition('\u005F', '\u005F', state1),
				new Transition('\uFF65', '\uFF65', state1), new Transition('\u203F', '\u2040', state1),
				new Transition('\uFF3F', '\uFF3F', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut109() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u3030', '\u3030', state1), new Transition('\u002D', '\u002D', state1),
				new Transition('\u00AD', '\u00AD', state1), new Transition('\u058A', '\u058A', state1),
				new Transition('\uFF0D', '\uFF0D', state1), new Transition('\uFE63', '\uFE63', state1),
				new Transition('\u301C', '\u301C', state1), new Transition('\uFE31', '\uFE32', state1),
				new Transition('\uFE58', '\uFE58', state1), new Transition('\u1806', '\u1806', state1),
				new Transition('\u2010', '\u2015', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut110() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u300D', '\u300D', state1), new Transition('\uFE40', '\uFE40', state1),
				new Transition('\u208E', '\u208E', state1), new Transition('\uFE5A', '\uFE5A', state1),
				new Transition('\u301B', '\u301B', state1), new Transition('\uFE5C', '\uFE5C', state1),
				new Transition('\uFE36', '\uFE36', state1), new Transition('\u300F', '\u300F', state1),
				new Transition('\u169C', '\u169C', state1), new Transition('\u0029', '\u0029', state1),
				new Transition('\uFE42', '\uFE42', state1), new Transition('\uFE44', '\uFE44', state1),
				new Transition('\u005D', '\u005D', state1), new Transition('\u232A', '\u232A', state1),
				new Transition('\uFE5E', '\uFE5E', state1), new Transition('\u3011', '\u3011', state1),
				new Transition('\uFF5D', '\uFF5D', state1), new Transition('\u301E', '\u301F', state1),
				new Transition('\uFE38', '\uFE38', state1), new Transition('\u2046', '\u2046', state1),
				new Transition('\uFE3A', '\uFE3A', state1), new Transition('\u0F3B', '\u0F3B', state1),
				new Transition('\uFE3C', '\uFE3C', state1), new Transition('\u3015', '\u3015', state1),
				new Transition('\uFF09', '\uFF09', state1), new Transition('\uFF63', '\uFF63', state1),
				new Transition('\u3009', '\u3009', state1), new Transition('\u007D', '\u007D', state1),
				new Transition('\u0F3D', '\u0F3D', state1), new Transition('\uFE3E', '\uFE3E', state1),
				new Transition('\u3017', '\u3017', state1), new Transition('\uFF3D', '\uFF3D', state1),
				new Transition('\u207E', '\u207E', state1), new Transition('\u300B', '\u300B', state1),
				new Transition('\u3019', '\u3019', state1), new Transition('\uFD3F', '\uFD3F', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut111() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u201D', '\u201D', state1), new Transition('\u203A', '\u203A', state1),
				new Transition('\u00BB', '\u00BB', state1), new Transition('\u2019', '\u2019', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut112() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u00AB', '\u00AB', state1), new Transition('\u2018', '\u2018', state1),
				new Transition('\u201B', '\u201C', state1), new Transition('\u201F', '\u201F', state1),
				new Transition('\u2039', '\u2039', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut113() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u104A', '\u104F', state1), new Transition('\uFF1A', '\uFF1B', state1),
				new Transition('\u061B', '\u061B', state1), new Transition('\u2030', '\u2038', state1),
				new Transition('\u0E4F', '\u0E4F', state1), new Transition('\u3001', '\u3003', state1),
				new Transition('\uFE68', '\uFE68', state1), new Transition('\u1800', '\u1805', state1),
				new Transition('\uFF01', '\uFF03', state1), new Transition('\uFE6A', '\uFE6B', state1),
				new Transition('\uFE50', '\uFE52', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0F85', '\u0F85', state1), new Transition('\uFF1F', '\uFF20', state1),
				new Transition('\u061F', '\u061F', state1), new Transition('\u066A', '\u066D', state1),
				new Transition('\uFF05', '\uFF07', state1), new Transition('\u16EB', '\u16ED', state1),
				new Transition('\u0387', '\u0387', state1), new Transition('\u06D4', '\u06D4', state1),
				new Transition('\u166D', '\u166E', state1), new Transition('\u003A', '\u003B', state1),
				new Transition('\u00A1', '\u00A1', state1), new Transition('\u0700', '\u070D', state1),
				new Transition('\uFF3C', '\uFF3C', state1), new Transition('\uFE54', '\uFE57', state1),
				new Transition('\u0021', '\u0023', state1), new Transition('\u1807', '\u180A', state1),
				new Transition('\u0589', '\u0589', state1), new Transition('\u0970', '\u0970', state1),
				new Transition('\u203B', '\u203E', state1), new Transition('\u2020', '\u2027', state1),
				new Transition('\u05BE', '\u05BE', state1), new Transition('\u17D4', '\u17DA', state1),
				new Transition('\uFF0A', '\uFF0A', state1), new Transition('\uFF0C', '\uFF0C', state1),
				new Transition('\u00BF', '\u00BF', state1), new Transition('\u060C', '\u060C', state1),
				new Transition('\u003F', '\u0040', state1), new Transition('\u0F04', '\u0F12', state1),
				new Transition('\u0025', '\'', state1), new Transition('\u05C0', '\u05C0', state1),
				new Transition('\u05F3', '\u05F4', state1), new Transition('\u0DF4', '\u0DF4', state1),
				new Transition('\u0E5A', '\u0E5B', state1), new Transition('\u2041', '\u2043', state1),
				new Transition('\\', '\\', state1), new Transition('\u17DC', '\u17DC', state1),
				new Transition('\uFF0E', '\uFF0F', state1), new Transition('\u05C3', '\u05C3', state1),
				new Transition('\u055A', '\u055F', state1), new Transition('\u002A', '\u002A', state1),
				new Transition('\u002C', '\u002C', state1), new Transition('\uFF61', '\uFF61', state1),
				new Transition('\uFE5F', '\uFE61', state1), new Transition('\u10FB', '\u10FB', state1),
				new Transition('\u002E', '\u002F', state1), new Transition('\uFF64', '\uFF64', state1),
				new Transition('\u2016', '\u2017', state1), new Transition('\uFE49', '\uFE4C', state1),
				new Transition('\uFE30', '\uFE30', state1), new Transition('\u037E', '\u037E', state1),
				new Transition('\u0964', '\u0965', state1), new Transition('\u2048', '\u204D', state1),
				new Transition('\u1361', '\u1368', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut114() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		state0.alter(false, 0, new Transition('\uE000', '\uF8FF', state9), new Transition('\uDB80', '\uDBBE', state8),
				new Transition('\uDBC0', '\uDBFE', state4), new Transition('\uDBBF', '\uDBBF', state5),
				new Transition('\uDBFF', '\uDBFF', state1));
		state1.alter(false, 0, new Transition('\uDC00', '\uDFFD', state2));
		state2.alter(true, 0);
		state3.alter(true, 0);
		state4.alter(false, 0, new Transition('\uDC00', '\uDFFF', state3));
		state5.alter(false, 0, new Transition('\uDC00', '\uDFFD', state6));
		state6.alter(true, 0);
		state7.alter(true, 0);
		state8.alter(false, 0, new Transition('\uDC00', '\uDFFF', state7));
		state9.alter(true, 0);
		return Automaton.load(state0, false, null);
	}

	private static Automaton getAut115() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u208D', '\u208D', state1), new Transition('\uFE41', '\uFE41', state1),
				new Transition('\u201A', '\u201A', state1), new Transition('\u301A', '\u301A', state1),
				new Transition('\uFE5B', '\uFE5B', state1), new Transition('\uFF5B', '\uFF5B', state1),
				new Transition('\u300E', '\u300E', state1), new Transition('\u005B', '\u005B', state1),
				new Transition('\u169B', '\u169B', state1), new Transition('\u0028', '\u0028', state1),
				new Transition('\uFE43', '\uFE43', state1), new Transition('\u2329', '\u2329', state1),
				new Transition('\uFE35', '\uFE35', state1), new Transition('\u3010', '\u3010', state1),
				new Transition('\u301D', '\u301D', state1), new Transition('\uFE5D', '\uFE5D', state1),
				new Transition('\u201E', '\u201E', state1), new Transition('\uFE37', '\uFE37', state1),
				new Transition('\u2045', '\u2045', state1), new Transition('\uFE39', '\uFE39', state1),
				new Transition('\u0F3A', '\u0F3A', state1), new Transition('\uFE3B', '\uFE3B', state1),
				new Transition('\uFF3B', '\uFF3B', state1), new Transition('\u3014', '\u3014', state1),
				new Transition('\u007B', '\u007B', state1), new Transition('\u3008', '\u3008', state1),
				new Transition('\u0F3C', '\u0F3C', state1), new Transition('\uFF08', '\uFF08', state1),
				new Transition('\u3016', '\u3016', state1), new Transition('\uFF62', '\uFF62', state1),
				new Transition('\u207D', '\u207D', state1), new Transition('\u300A', '\u300A', state1),
				new Transition('\uFD3E', '\uFD3E', state1), new Transition('\uFE3D', '\uFE3D', state1),
				new Transition('\u3018', '\u3018', state1), new Transition('\uFE59', '\uFE59', state1),
				new Transition('\u300C', '\u300C', state1), new Transition('\uFE3F', '\uFE3F', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut116() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 0, new Transition('\u0B99', '\u0B9A', state3), new Transition('\u00C0', '\u00D6', state3),
				new Transition('\u0B9C', '\u0B9C', state3), new Transition('\u1F20', '\u1F45', state3),
				new Transition('\u119E', '\u119E', state3), new Transition('\u0B36', '\u0B39', state3),
				new Transition('\u06D0', '\u06D3', state3), new Transition('\u0B9E', '\u0B9F', state3),
				new Transition('\u1F80', '\u1FB4', state3), new Transition('\u0993', '\u09A8', state3),
				new Transition('\u0134', '\u013E', state3), new Transition('\u3007', '\u3007', state3),
				new Transition('\u0401', '\u040C', state3), new Transition('\u0A05', '\u0A0A', state3),
				new Transition('\u06D5', '\u06D5', state3), new Transition('\u1EA0', '\u1EF9', state3),
				new Transition('\u113C', '\u113C', state3), new Transition('\u0C05', '\u0C0C', state3),
				new Transition('\u0061', '\u007A', state3), new Transition('\u30A1', '\u30FA', state3),
				new Transition('\u093D', '\u093D', state3), new Transition('\u0B3D', '\u0B3D', state3),
				new Transition('\u0BA3', '\u0BA4', state3), new Transition('\u113E', '\u113E', state3),
				new Transition('\u01FA', '\u0217', state3), new Transition('\u3041', '\u3094', state3),
				new Transition('\u045E', '\u0481', state3), new Transition('\u0A72', '\u0A74', state3),
				new Transition('\u1140', '\u1140', state3), new Transition('\u11A8', '\u11A8', state3),
				new Transition('\u0180', '\u01C3', state3), new Transition('\u0C0E', '\u0C10', state3),
				new Transition('\u0A0F', '\u0A10', state3), new Transition('\u0BA8', '\u0BAA', state3),
				new Transition('\u0F40', '\u0F47', state3), new Transition('\u0CDE', '\u0CDE', state3),
				new Transition('\u11AB', '\u11AB', state3), new Transition('\u0141', '\u0148', state3),
				new Transition('\u0AE0', '\u0AE0', state3), new Transition('\u04D0', '\u04EB', state3),
				new Transition('\u0CE0', '\u0CE1', state3), new Transition('\u0531', '\u0556', state3),
				new Transition('\u09AA', '\u09B0', state3), new Transition('\u11AE', '\u11AF', state3),
				new Transition('\u1F48', '\u1F4D', state3), new Transition('\u09B2', '\u09B2', state3),
				new Transition('\u0BAE', '\u0BB5', state3), new Transition('\u06E5', '\u06E6', state3),
				new Transition('\u114C', '\u114C', state3), new Transition('\u10D0', '\u10F6', state3),
				new Transition('\u114E', '\u114E', state3), new Transition('\u0E81', '\u0E82', state3),
				new Transition('\u0E01', '\u0E2E', state3), new Transition('\u1150', '\u1150', state3),
				new Transition('\u00D8', '\u00F6', state3), new Transition('\u0E84', '\u0E84', state3),
				new Transition('\u11B7', '\u11B8', state3), new Transition('\u09B6', '\u09B9', state3),
				new Transition('\u0250', '\u02A8', state3), new Transition('\u0BB7', '\u0BB9', state3),
				new Transition('\u0C12', '\u0C28', state3), new Transition('\u0A13', '\u0A28', state3),
				new Transition('\u1FB6', '\u1FBC', state3), new Transition('\u11BA', '\u11BA', state3),
				new Transition('\u1F50', '\u1F57', state3), new Transition('\u0E87', '\u0E88', state3),
				new Transition('\u1154', '\u1155', state3), new Transition('\u0A85', '\u0A8B', state3),
				new Transition('\u0C85', '\u0C8C', state3), new Transition('\u03A3', '\u03CE', state3),
				new Transition('\u0E8A', '\u0E8A', state3), new Transition('\u1FBE', '\u1FBE', state3),
				new Transition('\u04EE', '\u04F5', state3), new Transition('\u0559', '\u0559', state3),
				new Transition('\u1159', '\u1159', state3), new Transition('\u1F59', '\u1F59', state3),
				new Transition('\u3021', '\u3029', state3), new Transition('\u11BC', '\u11C2', state3),
				new Transition('\u0A8D', '\u0A8D', state3), new Transition('\u0E8D', '\u0E8D', state3),
				new Transition('\u1F5B', '\u1F5B', state3), new Transition('\u0C8E', '\u0C90', state3),
				new Transition('\u0F49', '\u0F69', state3), new Transition('\u0B5C', '\u0B5D', state3),
				new Transition('\u1FC2', '\u1FC4', state3), new Transition('\u0A8F', '\u0A91', state3),
				new Transition('\u1F5D', '\u1F5D', state3), new Transition('\u0958', '\u0961', state3),
				new Transition('\u04F8', '\u04F9', state3), new Transition('\u0B5F', '\u0B61', state3),
				new Transition('\u115F', '\u1161', state3), new Transition('\u0D60', '\u0D61', state3),
				new Transition('\u0A2A', '\u0A30', state3), new Transition('\uAC00', '\uD7A3', state3),
				new Transition('\u0C2A', '\u0C33', state3), new Transition('\u0E94', '\u0E97', state3),
				new Transition('\u4E00', '\u9FA5', state3), new Transition('\u1163', '\u1163', state3),
				new Transition('\u0621', '\u063A', state3), new Transition('\u0E30', '\u0E30', state3),
				new Transition('\u1FC6', '\u1FCC', state3), new Transition('\u1165', '\u1165', state3),
				new Transition('\u0A32', '\u0A33', state3), new Transition('\u0E32', '\u0E33', state3),
				new Transition('\u1100', '\u1100', state3), new Transition('\u1167', '\u1167', state3),
				new Transition('\u0671', '\u06B7', state3), new Transition('\u040E', '\u044F', state3),
				new Transition('\u0A35', '\u0A36', state3), new Transition('\u1102', '\u1103', state3),
				new Transition('\u1169', '\u1169', state3), new Transition('\u014A', '\u017E', state3),
				new Transition('\u0E99', '\u0E9F', state3), new Transition('\u0C35', '\u0C39', state3),
				new Transition('\u1FD0', '\u1FD3', state3), new Transition('\u0A38', '\u0A39', state3),
				new Transition('\u0C92', '\u0CA8', state3), new Transition('\u0A93', '\u0AA8', state3),
				new Transition('\u1105', '\u1107', state3), new Transition('\u03D0', '\u03D6', state3),
				new Transition('\u116D', '\u116E', state3), new Transition('\u0EA1', '\u0EA3', state3),
				new Transition('\u1109', '\u1109', state3), new Transition('\u0B05', '\u0B0C', state3),
				new Transition('\u0D05', '\u0D0C', state3), new Transition('\u1F5F', '\u1F7D', state3),
				new Transition('\u0EA5', '\u0EA5', state3), new Transition('\u110B', '\u110C', state3),
				new Transition('\u1172', '\u1173', state3), new Transition('\u1FD6', '\u1FDB', state3),
				new Transition('\u1F00', '\u1F15', state3), new Transition('\u03DA', '\u03DA', state3),
				new Transition('\u0EA7', '\u0EA7', state3), new Transition('\u1175', '\u1175', state3),
				new Transition('\u03DC', '\u03DC', state3), new Transition('\u0D0E', '\u0D10', state3),
				new Transition('\u0B0F', '\u0B10', state3), new Transition('\u09DC', '\u09DD', state3),
				new Transition('\u0E40', '\u0E45', state3), new Transition('\u110E', '\u1112', state3),
				new Transition('\u0561', '\u0586', state3), new Transition('\u0EAA', '\u0EAB', state3),
				new Transition('\u03DE', '\u03DE', state3), new Transition('\u05D0', '\u05EA', state3),
				new Transition('\u03E0', '\u03E0', state3), new Transition('\u0641', '\u064A', state3),
				new Transition('\u09DF', '\u09E1', state3), new Transition('\u0AAA', '\u0AB0', state3),
				new Transition('\u0EAD', '\u0EAE', state3), new Transition('\u01CD', '\u01F0', state3),
				new Transition('\u0490', '\u04C4', state3), new Transition('\u0CAA', '\u0CB3', state3),
				new Transition('\u0EB0', '\u0EB0', state3), new Transition('\u0AB2', '\u0AB3', state3),
				new Transition('\u0EB2', '\u0EB3', state3), new Transition('\u00F8', '\u0131', state3),
				new Transition('\u1FE0', '\u1FEC', state3), new Transition('\u2180', '\u2182', state3),
				new Transition('\u1F18', '\u1F1D', state3), new Transition('\u3105', '\u312C', state3),
				new Transition('\u10A0', '\u10C5', state3), new Transition('\u0041', '\u005A', state3),
				new Transition('\u0AB5', '\u0AB9', state3), new Transition('\u0CB5', '\u0CB9', state3),
				new Transition('\u11EB', '\u11EB', state3), new Transition('\u0D12', '\u0D28', state3),
				new Transition('\u03E2', '\u03F3', state3), new Transition('\u0386', '\u0386', state3),
				new Transition('\u0B13', '\u0B28', state3), new Transition('\u0B85', '\u0B8A', state3),
				new Transition('\u0388', '\u038A', state3), new Transition('\u06BA', '\u06BE', state3),
				new Transition('\u0985', '\u098C', state3), new Transition('\u11F0', '\u11F0', state3),
				new Transition('\u0ABD', '\u0ABD', state3), new Transition('\u0EBD', '\u0EBD', state3),
				new Transition('\u09F0', '\u09F1', state3), new Transition('\u0905', '\u0939', state3),
				new Transition('\u0451', '\u045C', state3), new Transition('\u05F0', '\u05F2', state3),
				new Transition('\u02BB', '\u02C1', state3), new Transition('\u038C', '\u038C', state3),
				new Transition('\u2126', '\u2126', state3), new Transition('\u1FF2', '\u1FF4', state3),
				new Transition('\u0A59', '\u0A5C', state3), new Transition('\u01F4', '\u01F5', state3),
				new Transition('\u0B8E', '\u0B90', state3), new Transition('\u0EC0', '\u0EC4', state3),
				new Transition('\u098F', '\u0990', state3), new Transition('\u1E00', '\u1E9B', state3),
				new Transition('\u212A', '\u212B', state3), new Transition('\u0A5E', '\u0A5E', state3),
				new Transition('\u005F', '\u005F', state3), new Transition('\u11F9', '\u11F9', state3),
				new Transition('\u1FF6', '\u1FFC', state3), new Transition('\u0B92', '\u0B95', state3),
				new Transition('\u0C60', '\u0C61', state3), new Transition('\u0B2A', '\u0B30', state3),
				new Transition('\u04C7', '\u04C8', state3), new Transition('\u212E', '\u212E', state3),
				new Transition('\u06C0', '\u06CE', state3), new Transition('\u04CB', '\u04CC', state3),
				new Transition('\u0B32', '\u0B33', state3), new Transition('\u038E', '\u03A1', state3),
				new Transition('\u0D2A', '\u0D39', state3));
		state1.alter(true, 2, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0591', '\u05A1', state1), new Transition('\u0F35', '\u0F35', state1),
				new Transition('\u0A02', '\u0A02', state1), new Transition('\u0C01', '\u0C03', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u02D0', '\u02D1', state1), new Transition('\u0F37', '\u0F37', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u0C66', '\u0C6F', state1), new Transition('\u3005', '\u3005', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0F39', '\u0F39', state1),
				new Transition('\u0993', '\u09A8', state1), new Transition('\u0134', '\u013E', state1),
				new Transition('\u3007', '\u3007', state1), new Transition('\u0401', '\u040C', state1),
				new Transition('\u0A05', '\u0A0A', state1), new Transition('\u0A66', '\u0A74', state1),
				new Transition('\u0ED0', '\u0ED9', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u0CD5', '\u0CD6', state1), new Transition('\u113C', '\u113C', state1),
				new Transition('\u0C05', '\u0C0C', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u30A1', '\u30FA', state1), new Transition('\u0BA3', '\u0BA4', state1),
				new Transition('\u20D0', '\u20DC', state1), new Transition('\u113E', '\u113E', state1),
				new Transition('\u01FA', '\u0217', state1), new Transition('\u0F99', '\u0FAD', state1),
				new Transition('\u3041', '\u3094', state1), new Transition('\u045E', '\u0481', state1),
				new Transition('\u1140', '\u1140', state1), new Transition('\u0B3C', '\u0B43', state1),
				new Transition('\u0D3E', '\u0D43', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F3E', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u093C', '\u094D', state1), new Transition('\u0AE0', '\u0AE0', state1),
				new Transition('\u04D0', '\u04EB', state1), new Transition('\u06D5', '\u06E8', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u0D46', '\u0D48', state1),
				new Transition('\u20E1', '\u20E1', state1), new Transition('\u0B47', '\u0B48', state1),
				new Transition('\u11AE', '\u11AF', state1), new Transition('\u05A3', '\u05B9', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u0D4A', '\u0D4D', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u0B4B', '\u0B4D', state1),
				new Transition('\u10D0', '\u10F6', state1), new Transition('\u114E', '\u114E', state1),
				new Transition('\u0FB1', '\u0FB7', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0A81', '\u0A83', state1), new Transition('\u0E01', '\u0E2E', state1),
				new Transition('\u0C82', '\u0C83', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0483', '\u0486', state1),
				new Transition('\u0AE6', '\u0AEF', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0CE6', '\u0CEF', state1), new Transition('\u06EA', '\u06ED', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0FB9', '\u0FB9', state1),
				new Transition('\u0951', '\u0954', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u11BA', '\u11BA', state1),
				new Transition('\u1F50', '\u1F57', state1), new Transition('\u0E87', '\u0E88', state1),
				new Transition('\u1154', '\u1155', state1), new Transition('\u0A85', '\u0A8B', state1),
				new Transition('\u09BC', '\u09BC', state1), new Transition('\u05BB', '\u05BD', state1),
				new Transition('\u0C85', '\u0C8C', state1), new Transition('\u03A3', '\u03CE', state1),
				new Transition('\u0B56', '\u0B57', state1), new Transition('\u0E8A', '\u0E8A', state1),
				new Transition('\u0D57', '\u0D57', state1), new Transition('\u1FBE', '\u1FBE', state1),
				new Transition('\u04EE', '\u04F5', state1), new Transition('\u05BF', '\u05BF', state1),
				new Transition('\u0559', '\u0559', state1), new Transition('\u1159', '\u1159', state1),
				new Transition('\u1F59', '\u1F59', state1), new Transition('\u11BC', '\u11C2', state1),
				new Transition('\u0A8D', '\u0A8D', state1), new Transition('\u0E8D', '\u0E8D', state1),
				new Transition('\u0BBE', '\u0BC2', state1), new Transition('\u1F5B', '\u1F5B', state1),
				new Transition('\u05C1', '\u05C2', state1), new Transition('\u09BE', '\u09C4', state1),
				new Transition('\u06F0', '\u06F9', state1), new Transition('\u0C8E', '\u0C90', state1),
				new Transition('\u0F49', '\u0F69', state1), new Transition('\u0B5C', '\u0B5D', state1),
				new Transition('\u3021', '\u302F', state1), new Transition('\u1FC2', '\u1FC4', state1),
				new Transition('\u0A8F', '\u0A91', state1), new Transition('\u1F5D', '\u1F5D', state1),
				new Transition('\u05C4', '\u05C4', state1), new Transition('\u0958', '\u0963', state1),
				new Transition('\u04F8', '\u04F9', state1), new Transition('\u0B5F', '\u0B61', state1),
				new Transition('\u115F', '\u1161', state1), new Transition('\u0360', '\u0361', state1),
				new Transition('\u0D60', '\u0D61', state1), new Transition('\u002D', '\u002E', state1),
				new Transition('\u0A2A', '\u0A30', state1), new Transition('\u0BC6', '\u0BC8', state1),
				new Transition('\u09C7', '\u09C8', state1), new Transition('\uAC00', '\uD7A3', state1),
				new Transition('\u0C2A', '\u0C33', state1), new Transition('\u0E94', '\u0E97', state1),
				new Transition('\u4E00', '\u9FA5', state1), new Transition('\u1163', '\u1163', state1),
				new Transition('\u0621', '\u063A', state1), new Transition('\u1FC6', '\u1FCC', state1),
				new Transition('\u30FC', '\u30FE', state1), new Transition('\u1165', '\u1165', state1),
				new Transition('\u0BCA', '\u0BCD', state1), new Transition('\u09CB', '\u09CD', state1),
				new Transition('\u0A32', '\u0A33', state1), new Transition('\u1100', '\u1100', state1),
				new Transition('\u3099', '\u309A', state1), new Transition('\u3031', '\u3035', state1),
				new Transition('\u1167', '\u1167', state1), new Transition('\u0670', '\u06B7', state1),
				new Transition('\u040E', '\u044F', state1), new Transition('\u0030', '\u0039', state1),
				new Transition('\u0901', '\u0903', state1), new Transition('\u0B01', '\u0B03', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u0D02', '\u0D03', state1),
				new Transition('\u1102', '\u1103', state1), new Transition('\u1169', '\u1169', state1),
				new Transition('\u014A', '\u017E', state1), new Transition('\u0E30', '\u0E3A', state1),
				new Transition('\u0E99', '\u0E9F', state1), new Transition('\u309D', '\u309E', state1),
				new Transition('\u0C35', '\u0C39', state1), new Transition('\u0966', '\u096F', state1),
				new Transition('\u0B66', '\u0B6F', state1), new Transition('\u0D66', '\u0D6F', state1),
				new Transition('\u1FD0', '\u1FD3', state1), new Transition('\u0A38', '\u0A39', state1),
				new Transition('\u0C92', '\u0CA8', state1), new Transition('\u0A93', '\u0AA8', state1),
				new Transition('\u1105', '\u1107', state1), new Transition('\u003A', '\u003A', state0),
				new Transition('\u03D0', '\u03D6', state1), new Transition('\u116D', '\u116E', state1),
				new Transition('\u0EA1', '\u0EA3', state1), new Transition('\u0A3C', '\u0A3C', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u09D7', '\u09D7', state1),
				new Transition('\u0BD7', '\u0BD7', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0A3E', '\u0A42', state1), new Transition('\u0EA7', '\u0EA7', state1),
				new Transition('\u0C3E', '\u0C44', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u110E', '\u1112', state1), new Transition('\u0561', '\u0586', state1),
				new Transition('\u0EAA', '\u0EAB', state1), new Transition('\u03DE', '\u03DE', state1),
				new Transition('\u05D0', '\u05EA', state1), new Transition('\u03E0', '\u03E0', state1),
				new Transition('\u0AAA', '\u0AB0', state1), new Transition('\u0C46', '\u0C48', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u0A47', '\u0A48', state1),
				new Transition('\u09DF', '\u09E3', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0E40', '\u0E4E', state1), new Transition('\u0490', '\u04C4', state1),
				new Transition('\u0CAA', '\u0CB3', state1), new Transition('\u0F71', '\u0F84', state1),
				new Transition('\u0640', '\u0652', state1), new Transition('\u0C4A', '\u0C4D', state1),
				new Transition('\u0F18', '\u0F19', state1), new Transition('\u0A4B', '\u0A4D', state1),
				new Transition('\u0AB2', '\u0AB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u0981', '\u0983', state1),
				new Transition('\u0EB0', '\u0EB9', state1), new Transition('\u0B82', '\u0B83', state1),
				new Transition('\u3105', '\u312C', state1), new Transition('\u10A0', '\u10C5', state1),
				new Transition('\u0041', '\u005A', state1), new Transition('\u00B7', '\u00B7', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0BE7', '\u0BEF', state1),
				new Transition('\u0D12', '\u0D28', state1), new Transition('\u03E2', '\u03F3', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u09E6', '\u09F1', state1),
				new Transition('\u0B85', '\u0B8A', state1), new Transition('\u0386', '\u038A', state1),
				new Transition('\u0E50', '\u0E59', state1), new Transition('\u0C55', '\u0C56', state1),
				new Transition('\u0EBB', '\u0EBD', state1), new Transition('\u0F86', '\u0F8B', state1),
				new Transition('\u06BA', '\u06BE', state1), new Transition('\u0985', '\u098C', state1),
				new Transition('\u11F0', '\u11F0', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u0F20', '\u0F29', state1),
				new Transition('\u038C', '\u038C', state1), new Transition('\u2126', '\u2126', state1),
				new Transition('\u1FF2', '\u1FF4', state1), new Transition('\u0A59', '\u0A5C', state1),
				new Transition('\u01F4', '\u01F5', state1), new Transition('\u0ABC', '\u0AC5', state1),
				new Transition('\u0CBE', '\u0CC4', state1), new Transition('\u0B8E', '\u0B90', state1),
				new Transition('\u0EC0', '\u0EC4', state1), new Transition('\u098F', '\u0990', state1),
				new Transition('\u0300', '\u0345', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u0EC6', '\u0EC6', state1), new Transition('\u0F90', '\u0F95', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u0CC6', '\u0CC8', state1), new Transition('\u04C7', '\u04C8', state1),
				new Transition('\u212E', '\u212E', state1), new Transition('\u0AC7', '\u0AC9', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u0F97', '\u0F97', state1),
				new Transition('\u0EC8', '\u0ECD', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0660', '\u0669', state1), new Transition('\u0CCA', '\u0CCD', state1),
				new Transition('\u0ACB', '\u0ACD', state1), new Transition('\u0B32', '\u0B33', state1),
				new Transition('\u038E', '\u03A1', state1), new Transition('\u0D2A', '\u0D39', state1));
		state2.alter(false, 1, new Transition('\u0B99', '\u0B9A', state1), new Transition('\u00C0', '\u00D6', state1),
				new Transition('\u0B9C', '\u0B9C', state1), new Transition('\u1F20', '\u1F45', state1),
				new Transition('\u119E', '\u119E', state1), new Transition('\u0B36', '\u0B39', state1),
				new Transition('\u06D0', '\u06D3', state1), new Transition('\u0B9E', '\u0B9F', state1),
				new Transition('\u1F80', '\u1FB4', state1), new Transition('\u0993', '\u09A8', state1),
				new Transition('\u0134', '\u013E', state1), new Transition('\u3007', '\u3007', state1),
				new Transition('\u0401', '\u040C', state1), new Transition('\u0A05', '\u0A0A', state1),
				new Transition('\u06D5', '\u06D5', state1), new Transition('\u1EA0', '\u1EF9', state1),
				new Transition('\u113C', '\u113C', state1), new Transition('\u0C05', '\u0C0C', state1),
				new Transition('\u0061', '\u007A', state1), new Transition('\u30A1', '\u30FA', state1),
				new Transition('\u093D', '\u093D', state1), new Transition('\u0B3D', '\u0B3D', state1),
				new Transition('\u0BA3', '\u0BA4', state1), new Transition('\u113E', '\u113E', state1),
				new Transition('\u01FA', '\u0217', state1), new Transition('\u3041', '\u3094', state1),
				new Transition('\u045E', '\u0481', state1), new Transition('\u0A72', '\u0A74', state1),
				new Transition('\u1140', '\u1140', state1), new Transition('\u11A8', '\u11A8', state1),
				new Transition('\u0180', '\u01C3', state1), new Transition('\u0C0E', '\u0C10', state1),
				new Transition('\u0A0F', '\u0A10', state1), new Transition('\u0BA8', '\u0BAA', state1),
				new Transition('\u0F40', '\u0F47', state1), new Transition('\u0CDE', '\u0CDE', state1),
				new Transition('\u11AB', '\u11AB', state1), new Transition('\u0141', '\u0148', state1),
				new Transition('\u0AE0', '\u0AE0', state1), new Transition('\u04D0', '\u04EB', state1),
				new Transition('\u0CE0', '\u0CE1', state1), new Transition('\u0531', '\u0556', state1),
				new Transition('\u09AA', '\u09B0', state1), new Transition('\u11AE', '\u11AF', state1),
				new Transition('\u1F48', '\u1F4D', state1), new Transition('\u09B2', '\u09B2', state1),
				new Transition('\u0BAE', '\u0BB5', state1), new Transition('\u06E5', '\u06E6', state1),
				new Transition('\u114C', '\u114C', state1), new Transition('\u10D0', '\u10F6', state1),
				new Transition('\u114E', '\u114E', state1), new Transition('\u0E81', '\u0E82', state1),
				new Transition('\u0E01', '\u0E2E', state1), new Transition('\u1150', '\u1150', state1),
				new Transition('\u00D8', '\u00F6', state1), new Transition('\u0E84', '\u0E84', state1),
				new Transition('\u11B7', '\u11B8', state1), new Transition('\u09B6', '\u09B9', state1),
				new Transition('\u0250', '\u02A8', state1), new Transition('\u0BB7', '\u0BB9', state1),
				new Transition('\u0C12', '\u0C28', state1), new Transition('\u0A13', '\u0A28', state1),
				new Transition('\u1FB6', '\u1FBC', state1), new Transition('\u11BA', '\u11BA', state1),
				new Transition('\u1F50', '\u1F57', state1), new Transition('\u0E87', '\u0E88', state1),
				new Transition('\u1154', '\u1155', state1), new Transition('\u0A85', '\u0A8B', state1),
				new Transition('\u0C85', '\u0C8C', state1), new Transition('\u03A3', '\u03CE', state1),
				new Transition('\u0E8A', '\u0E8A', state1), new Transition('\u1FBE', '\u1FBE', state1),
				new Transition('\u04EE', '\u04F5', state1), new Transition('\u0559', '\u0559', state1),
				new Transition('\u1159', '\u1159', state1), new Transition('\u1F59', '\u1F59', state1),
				new Transition('\u3021', '\u3029', state1), new Transition('\u11BC', '\u11C2', state1),
				new Transition('\u0A8D', '\u0A8D', state1), new Transition('\u0E8D', '\u0E8D', state1),
				new Transition('\u1F5B', '\u1F5B', state1), new Transition('\u0C8E', '\u0C90', state1),
				new Transition('\u0F49', '\u0F69', state1), new Transition('\u0B5C', '\u0B5D', state1),
				new Transition('\u1FC2', '\u1FC4', state1), new Transition('\u0A8F', '\u0A91', state1),
				new Transition('\u1F5D', '\u1F5D', state1), new Transition('\u0958', '\u0961', state1),
				new Transition('\u04F8', '\u04F9', state1), new Transition('\u0B5F', '\u0B61', state1),
				new Transition('\u115F', '\u1161', state1), new Transition('\u0D60', '\u0D61', state1),
				new Transition('\u0A2A', '\u0A30', state1), new Transition('\uAC00', '\uD7A3', state1),
				new Transition('\u0C2A', '\u0C33', state1), new Transition('\u0E94', '\u0E97', state1),
				new Transition('\u4E00', '\u9FA5', state1), new Transition('\u1163', '\u1163', state1),
				new Transition('\u0621', '\u063A', state1), new Transition('\u0E30', '\u0E30', state1),
				new Transition('\u1FC6', '\u1FCC', state1), new Transition('\u1165', '\u1165', state1),
				new Transition('\u0A32', '\u0A33', state1), new Transition('\u0E32', '\u0E33', state1),
				new Transition('\u1100', '\u1100', state1), new Transition('\u1167', '\u1167', state1),
				new Transition('\u0671', '\u06B7', state1), new Transition('\u040E', '\u044F', state1),
				new Transition('\u0A35', '\u0A36', state1), new Transition('\u1102', '\u1103', state1),
				new Transition('\u1169', '\u1169', state1), new Transition('\u014A', '\u017E', state1),
				new Transition('\u0E99', '\u0E9F', state1), new Transition('\u0C35', '\u0C39', state1),
				new Transition('\u1FD0', '\u1FD3', state1), new Transition('\u0A38', '\u0A39', state1),
				new Transition('\u0C92', '\u0CA8', state1), new Transition('\u0A93', '\u0AA8', state1),
				new Transition('\u1105', '\u1107', state1), new Transition('\u03D0', '\u03D6', state1),
				new Transition('\u116D', '\u116E', state1), new Transition('\u0EA1', '\u0EA3', state1),
				new Transition('\u1109', '\u1109', state1), new Transition('\u0B05', '\u0B0C', state1),
				new Transition('\u0D05', '\u0D0C', state1), new Transition('\u1F5F', '\u1F7D', state1),
				new Transition('\u0EA5', '\u0EA5', state1), new Transition('\u110B', '\u110C', state1),
				new Transition('\u1172', '\u1173', state1), new Transition('\u1FD6', '\u1FDB', state1),
				new Transition('\u1F00', '\u1F15', state1), new Transition('\u03DA', '\u03DA', state1),
				new Transition('\u0EA7', '\u0EA7', state1), new Transition('\u1175', '\u1175', state1),
				new Transition('\u03DC', '\u03DC', state1), new Transition('\u0D0E', '\u0D10', state1),
				new Transition('\u0B0F', '\u0B10', state1), new Transition('\u09DC', '\u09DD', state1),
				new Transition('\u0E40', '\u0E45', state1), new Transition('\u110E', '\u1112', state1),
				new Transition('\u0561', '\u0586', state1), new Transition('\u0EAA', '\u0EAB', state1),
				new Transition('\u03DE', '\u03DE', state1), new Transition('\u05D0', '\u05EA', state1),
				new Transition('\u03E0', '\u03E0', state1), new Transition('\u0641', '\u064A', state1),
				new Transition('\u09DF', '\u09E1', state1), new Transition('\u0AAA', '\u0AB0', state1),
				new Transition('\u0EAD', '\u0EAE', state1), new Transition('\u01CD', '\u01F0', state1),
				new Transition('\u0490', '\u04C4', state1), new Transition('\u0CAA', '\u0CB3', state1),
				new Transition('\u0EB0', '\u0EB0', state1), new Transition('\u0AB2', '\u0AB3', state1),
				new Transition('\u0EB2', '\u0EB3', state1), new Transition('\u00F8', '\u0131', state1),
				new Transition('\u1FE0', '\u1FEC', state1), new Transition('\u2180', '\u2182', state1),
				new Transition('\u1F18', '\u1F1D', state1), new Transition('\u3105', '\u312C', state1),
				new Transition('\u10A0', '\u10C5', state1), new Transition('\u0041', '\u005A', state1),
				new Transition('\u0AB5', '\u0AB9', state1), new Transition('\u0CB5', '\u0CB9', state1),
				new Transition('\u11EB', '\u11EB', state1), new Transition('\u0D12', '\u0D28', state1),
				new Transition('\u03E2', '\u03F3', state1), new Transition('\u0386', '\u0386', state1),
				new Transition('\u0B13', '\u0B28', state1), new Transition('\u0B85', '\u0B8A', state1),
				new Transition('\u0388', '\u038A', state1), new Transition('\u06BA', '\u06BE', state1),
				new Transition('\u0985', '\u098C', state1), new Transition('\u11F0', '\u11F0', state1),
				new Transition('\u0ABD', '\u0ABD', state1), new Transition('\u0EBD', '\u0EBD', state1),
				new Transition('\u09F0', '\u09F1', state1), new Transition('\u0905', '\u0939', state1),
				new Transition('\u0451', '\u045C', state1), new Transition('\u05F0', '\u05F2', state1),
				new Transition('\u02BB', '\u02C1', state1), new Transition('\u038C', '\u038C', state1),
				new Transition('\u2126', '\u2126', state1), new Transition('\u1FF2', '\u1FF4', state1),
				new Transition('\u0A59', '\u0A5C', state1), new Transition('\u01F4', '\u01F5', state1),
				new Transition('\u0B8E', '\u0B90', state1), new Transition('\u0EC0', '\u0EC4', state1),
				new Transition('\u098F', '\u0990', state1), new Transition('\u1E00', '\u1E9B', state1),
				new Transition('\u212A', '\u212B', state1), new Transition('\u0A5E', '\u0A5E', state1),
				new Transition('\u005F', '\u005F', state1), new Transition('\u11F9', '\u11F9', state1),
				new Transition('\u1FF6', '\u1FFC', state1), new Transition('\u0B92', '\u0B95', state1),
				new Transition('\u0C60', '\u0C61', state1), new Transition('\u0B2A', '\u0B30', state1),
				new Transition('\u04C7', '\u04C8', state1), new Transition('\u212E', '\u212E', state1),
				new Transition('\u06C0', '\u06CE', state1), new Transition('\u04CB', '\u04CC', state1),
				new Transition('\u0B32', '\u0B33', state1), new Transition('\u038E', '\u03A1', state1),
				new Transition('\u0D2A', '\u0D39', state1));
		state3.alter(true, 3, new Transition('\u0B99', '\u0B9A', state3), new Transition('\u00C0', '\u00D6', state3),
				new Transition('\u0591', '\u05A1', state3), new Transition('\u0F35', '\u0F35', state3),
				new Transition('\u0A02', '\u0A02', state3), new Transition('\u0C01', '\u0C03', state3),
				new Transition('\u0B9C', '\u0B9C', state3), new Transition('\u1F20', '\u1F45', state3),
				new Transition('\u02D0', '\u02D1', state3), new Transition('\u0F37', '\u0F37', state3),
				new Transition('\u119E', '\u119E', state3), new Transition('\u0B36', '\u0B39', state3),
				new Transition('\u06D0', '\u06D3', state3), new Transition('\u0B9E', '\u0B9F', state3),
				new Transition('\u0C66', '\u0C6F', state3), new Transition('\u3005', '\u3005', state3),
				new Transition('\u1F80', '\u1FB4', state3), new Transition('\u0F39', '\u0F39', state3),
				new Transition('\u0993', '\u09A8', state3), new Transition('\u0134', '\u013E', state3),
				new Transition('\u3007', '\u3007', state3), new Transition('\u0401', '\u040C', state3),
				new Transition('\u0A05', '\u0A0A', state3), new Transition('\u0A66', '\u0A74', state3),
				new Transition('\u0ED0', '\u0ED9', state3), new Transition('\u1EA0', '\u1EF9', state3),
				new Transition('\u0CD5', '\u0CD6', state3), new Transition('\u113C', '\u113C', state3),
				new Transition('\u0C05', '\u0C0C', state3), new Transition('\u0061', '\u007A', state3),
				new Transition('\u30A1', '\u30FA', state3), new Transition('\u0BA3', '\u0BA4', state3),
				new Transition('\u20D0', '\u20DC', state3), new Transition('\u113E', '\u113E', state3),
				new Transition('\u01FA', '\u0217', state3), new Transition('\u0F99', '\u0FAD', state3),
				new Transition('\u3041', '\u3094', state3), new Transition('\u045E', '\u0481', state3),
				new Transition('\u1140', '\u1140', state3), new Transition('\u0B3C', '\u0B43', state3),
				new Transition('\u0D3E', '\u0D43', state3), new Transition('\u11A8', '\u11A8', state3),
				new Transition('\u0180', '\u01C3', state3), new Transition('\u0C0E', '\u0C10', state3),
				new Transition('\u0A0F', '\u0A10', state3), new Transition('\u0BA8', '\u0BAA', state3),
				new Transition('\u0F3E', '\u0F47', state3), new Transition('\u0CDE', '\u0CDE', state3),
				new Transition('\u11AB', '\u11AB', state3), new Transition('\u0141', '\u0148', state3),
				new Transition('\u093C', '\u094D', state3), new Transition('\u0AE0', '\u0AE0', state3),
				new Transition('\u04D0', '\u04EB', state3), new Transition('\u06D5', '\u06E8', state3),
				new Transition('\u0CE0', '\u0CE1', state3), new Transition('\u0531', '\u0556', state3),
				new Transition('\u09AA', '\u09B0', state3), new Transition('\u0D46', '\u0D48', state3),
				new Transition('\u20E1', '\u20E1', state3), new Transition('\u0B47', '\u0B48', state3),
				new Transition('\u11AE', '\u11AF', state3), new Transition('\u05A3', '\u05B9', state3),
				new Transition('\u1F48', '\u1F4D', state3), new Transition('\u09B2', '\u09B2', state3),
				new Transition('\u0BAE', '\u0BB5', state3), new Transition('\u0D4A', '\u0D4D', state3),
				new Transition('\u114C', '\u114C', state3), new Transition('\u0B4B', '\u0B4D', state3),
				new Transition('\u10D0', '\u10F6', state3), new Transition('\u114E', '\u114E', state3),
				new Transition('\u0FB1', '\u0FB7', state3), new Transition('\u0E81', '\u0E82', state3),
				new Transition('\u0A81', '\u0A83', state3), new Transition('\u0E01', '\u0E2E', state3),
				new Transition('\u0C82', '\u0C83', state3), new Transition('\u1150', '\u1150', state3),
				new Transition('\u00D8', '\u00F6', state3), new Transition('\u0E84', '\u0E84', state3),
				new Transition('\u11B7', '\u11B8', state3), new Transition('\u09B6', '\u09B9', state3),
				new Transition('\u0250', '\u02A8', state3), new Transition('\u0483', '\u0486', state3),
				new Transition('\u0AE6', '\u0AEF', state3), new Transition('\u0BB7', '\u0BB9', state3),
				new Transition('\u0CE6', '\u0CEF', state3), new Transition('\u06EA', '\u06ED', state3),
				new Transition('\u0C12', '\u0C28', state3), new Transition('\u0FB9', '\u0FB9', state3),
				new Transition('\u0951', '\u0954', state3), new Transition('\u0A13', '\u0A28', state3),
				new Transition('\u1FB6', '\u1FBC', state3), new Transition('\u11BA', '\u11BA', state3),
				new Transition('\u1F50', '\u1F57', state3), new Transition('\u0E87', '\u0E88', state3),
				new Transition('\u1154', '\u1155', state3), new Transition('\u0A85', '\u0A8B', state3),
				new Transition('\u09BC', '\u09BC', state3), new Transition('\u05BB', '\u05BD', state3),
				new Transition('\u0C85', '\u0C8C', state3), new Transition('\u03A3', '\u03CE', state3),
				new Transition('\u0B56', '\u0B57', state3), new Transition('\u0E8A', '\u0E8A', state3),
				new Transition('\u0D57', '\u0D57', state3), new Transition('\u1FBE', '\u1FBE', state3),
				new Transition('\u04EE', '\u04F5', state3), new Transition('\u05BF', '\u05BF', state3),
				new Transition('\u0559', '\u0559', state3), new Transition('\u1159', '\u1159', state3),
				new Transition('\u1F59', '\u1F59', state3), new Transition('\u11BC', '\u11C2', state3),
				new Transition('\u0A8D', '\u0A8D', state3), new Transition('\u0E8D', '\u0E8D', state3),
				new Transition('\u0BBE', '\u0BC2', state3), new Transition('\u1F5B', '\u1F5B', state3),
				new Transition('\u05C1', '\u05C2', state3), new Transition('\u09BE', '\u09C4', state3),
				new Transition('\u06F0', '\u06F9', state3), new Transition('\u0C8E', '\u0C90', state3),
				new Transition('\u0F49', '\u0F69', state3), new Transition('\u0B5C', '\u0B5D', state3),
				new Transition('\u3021', '\u302F', state3), new Transition('\u1FC2', '\u1FC4', state3),
				new Transition('\u0A8F', '\u0A91', state3), new Transition('\u1F5D', '\u1F5D', state3),
				new Transition('\u05C4', '\u05C4', state3), new Transition('\u0958', '\u0963', state3),
				new Transition('\u04F8', '\u04F9', state3), new Transition('\u0B5F', '\u0B61', state3),
				new Transition('\u115F', '\u1161', state3), new Transition('\u0360', '\u0361', state3),
				new Transition('\u0D60', '\u0D61', state3), new Transition('\u002D', '\u002E', state3),
				new Transition('\u0A2A', '\u0A30', state3), new Transition('\u0BC6', '\u0BC8', state3),
				new Transition('\u09C7', '\u09C8', state3), new Transition('\uAC00', '\uD7A3', state3),
				new Transition('\u0C2A', '\u0C33', state3), new Transition('\u0E94', '\u0E97', state3),
				new Transition('\u4E00', '\u9FA5', state3), new Transition('\u1163', '\u1163', state3),
				new Transition('\u0621', '\u063A', state3), new Transition('\u1FC6', '\u1FCC', state3),
				new Transition('\u30FC', '\u30FE', state3), new Transition('\u1165', '\u1165', state3),
				new Transition('\u0BCA', '\u0BCD', state3), new Transition('\u09CB', '\u09CD', state3),
				new Transition('\u0A32', '\u0A33', state3), new Transition('\u1100', '\u1100', state3),
				new Transition('\u3099', '\u309A', state3), new Transition('\u3031', '\u3035', state3),
				new Transition('\u1167', '\u1167', state3), new Transition('\u0670', '\u06B7', state3),
				new Transition('\u040E', '\u044F', state3), new Transition('\u0030', '\u0039', state3),
				new Transition('\u0901', '\u0903', state3), new Transition('\u0B01', '\u0B03', state3),
				new Transition('\u0A35', '\u0A36', state3), new Transition('\u0D02', '\u0D03', state3),
				new Transition('\u1102', '\u1103', state3), new Transition('\u1169', '\u1169', state3),
				new Transition('\u014A', '\u017E', state3), new Transition('\u0E30', '\u0E3A', state3),
				new Transition('\u0E99', '\u0E9F', state3), new Transition('\u309D', '\u309E', state3),
				new Transition('\u0C35', '\u0C39', state3), new Transition('\u0966', '\u096F', state3),
				new Transition('\u0B66', '\u0B6F', state3), new Transition('\u0D66', '\u0D6F', state3),
				new Transition('\u1FD0', '\u1FD3', state3), new Transition('\u0A38', '\u0A39', state3),
				new Transition('\u0C92', '\u0CA8', state3), new Transition('\u0A93', '\u0AA8', state3),
				new Transition('\u1105', '\u1107', state3), new Transition('\u03D0', '\u03D6', state3),
				new Transition('\u116D', '\u116E', state3), new Transition('\u0EA1', '\u0EA3', state3),
				new Transition('\u0A3C', '\u0A3C', state3), new Transition('\u1109', '\u1109', state3),
				new Transition('\u0B05', '\u0B0C', state3), new Transition('\u0D05', '\u0D0C', state3),
				new Transition('\u09D7', '\u09D7', state3), new Transition('\u0BD7', '\u0BD7', state3),
				new Transition('\u1F5F', '\u1F7D', state3), new Transition('\u0EA5', '\u0EA5', state3),
				new Transition('\u110B', '\u110C', state3), new Transition('\u1172', '\u1173', state3),
				new Transition('\u1FD6', '\u1FDB', state3), new Transition('\u1F00', '\u1F15', state3),
				new Transition('\u03DA', '\u03DA', state3), new Transition('\u0A3E', '\u0A42', state3),
				new Transition('\u0EA7', '\u0EA7', state3), new Transition('\u0C3E', '\u0C44', state3),
				new Transition('\u1175', '\u1175', state3), new Transition('\u03DC', '\u03DC', state3),
				new Transition('\u0D0E', '\u0D10', state3), new Transition('\u0B0F', '\u0B10', state3),
				new Transition('\u09DC', '\u09DD', state3), new Transition('\u110E', '\u1112', state3),
				new Transition('\u0561', '\u0586', state3), new Transition('\u0EAA', '\u0EAB', state3),
				new Transition('\u03DE', '\u03DE', state3), new Transition('\u05D0', '\u05EA', state3),
				new Transition('\u03E0', '\u03E0', state3), new Transition('\u0AAA', '\u0AB0', state3),
				new Transition('\u0C46', '\u0C48', state3), new Transition('\u0EAD', '\u0EAE', state3),
				new Transition('\u0A47', '\u0A48', state3), new Transition('\u09DF', '\u09E3', state3),
				new Transition('\u01CD', '\u01F0', state3), new Transition('\u0E40', '\u0E4E', state3),
				new Transition('\u0490', '\u04C4', state3), new Transition('\u0CAA', '\u0CB3', state3),
				new Transition('\u0F71', '\u0F84', state3), new Transition('\u0640', '\u0652', state3),
				new Transition('\u0C4A', '\u0C4D', state3), new Transition('\u0F18', '\u0F19', state3),
				new Transition('\u0A4B', '\u0A4D', state3), new Transition('\u0AB2', '\u0AB3', state3),
				new Transition('\u00F8', '\u0131', state3), new Transition('\u1FE0', '\u1FEC', state3),
				new Transition('\u2180', '\u2182', state3), new Transition('\u1F18', '\u1F1D', state3),
				new Transition('\u0981', '\u0983', state3), new Transition('\u0EB0', '\u0EB9', state3),
				new Transition('\u0B82', '\u0B83', state3), new Transition('\u3105', '\u312C', state3),
				new Transition('\u10A0', '\u10C5', state3), new Transition('\u0041', '\u005A', state3),
				new Transition('\u00B7', '\u00B7', state3), new Transition('\u0AB5', '\u0AB9', state3),
				new Transition('\u0CB5', '\u0CB9', state3), new Transition('\u11EB', '\u11EB', state3),
				new Transition('\u0BE7', '\u0BEF', state3), new Transition('\u0D12', '\u0D28', state3),
				new Transition('\u03E2', '\u03F3', state3), new Transition('\u0B13', '\u0B28', state3),
				new Transition('\u09E6', '\u09F1', state3), new Transition('\u0B85', '\u0B8A', state3),
				new Transition('\u0386', '\u038A', state3), new Transition('\u0E50', '\u0E59', state3),
				new Transition('\u0C55', '\u0C56', state3), new Transition('\u0EBB', '\u0EBD', state3),
				new Transition('\u0F86', '\u0F8B', state3), new Transition('\u06BA', '\u06BE', state3),
				new Transition('\u0985', '\u098C', state3), new Transition('\u11F0', '\u11F0', state3),
				new Transition('\u0905', '\u0939', state3), new Transition('\u0451', '\u045C', state3),
				new Transition('\u05F0', '\u05F2', state3), new Transition('\u02BB', '\u02C1', state3),
				new Transition('\u0F20', '\u0F29', state3), new Transition('\u038C', '\u038C', state3),
				new Transition('\u2126', '\u2126', state3), new Transition('\u1FF2', '\u1FF4', state3),
				new Transition('\u0A59', '\u0A5C', state3), new Transition('\u01F4', '\u01F5', state3),
				new Transition('\u0ABC', '\u0AC5', state3), new Transition('\u0CBE', '\u0CC4', state3),
				new Transition('\u0B8E', '\u0B90', state3), new Transition('\u0EC0', '\u0EC4', state3),
				new Transition('\u098F', '\u0990', state3), new Transition('\u0300', '\u0345', state3),
				new Transition('\u1E00', '\u1E9B', state3), new Transition('\u212A', '\u212B', state3),
				new Transition('\u0A5E', '\u0A5E', state3), new Transition('\u005F', '\u005F', state3),
				new Transition('\u11F9', '\u11F9', state3), new Transition('\u0EC6', '\u0EC6', state3),
				new Transition('\u0F90', '\u0F95', state3), new Transition('\u1FF6', '\u1FFC', state3),
				new Transition('\u0B92', '\u0B95', state3), new Transition('\u0C60', '\u0C61', state3),
				new Transition('\u0B2A', '\u0B30', state3), new Transition('\u0CC6', '\u0CC8', state3),
				new Transition('\u04C7', '\u04C8', state3), new Transition('\u212E', '\u212E', state3),
				new Transition('\u0AC7', '\u0AC9', state3), new Transition('\u06C0', '\u06CE', state3),
				new Transition('\u0F97', '\u0F97', state3), new Transition('\u0EC8', '\u0ECD', state3),
				new Transition('\u04CB', '\u04CC', state3), new Transition('\u0660', '\u0669', state3),
				new Transition('\u0CCA', '\u0CCD', state3), new Transition('\u0ACB', '\u0ACD', state3),
				new Transition('\u0B32', '\u0B33', state3), new Transition('\u038E', '\u03A1', state3),
				new Transition('\u0D2A', '\u0D39', state3));
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut117() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		state0.alter(true, 1, new Transition('\u0B99', '\u0B9A', state0), new Transition('\u00C0', '\u00D6', state0),
				new Transition('\u0591', '\u05A1', state0), new Transition('\u0F35', '\u0F35', state0),
				new Transition('\u0A02', '\u0A02', state0), new Transition('\u0C01', '\u0C03', state0),
				new Transition('\u0B9C', '\u0B9C', state0), new Transition('\u1F20', '\u1F45', state0),
				new Transition('\u02D0', '\u02D1', state0), new Transition('\u0F37', '\u0F37', state0),
				new Transition('\u119E', '\u119E', state0), new Transition('\u0B36', '\u0B39', state0),
				new Transition('\u06D0', '\u06D3', state0), new Transition('\u0B9E', '\u0B9F', state0),
				new Transition('\u0C66', '\u0C6F', state0), new Transition('\u3005', '\u3005', state0),
				new Transition('\u1F80', '\u1FB4', state0), new Transition('\u0F39', '\u0F39', state0),
				new Transition('\u0993', '\u09A8', state0), new Transition('\u0134', '\u013E', state0),
				new Transition('\u3007', '\u3007', state0), new Transition('\u0401', '\u040C', state0),
				new Transition('\u0A05', '\u0A0A', state0), new Transition('\u0A66', '\u0A74', state0),
				new Transition('\u0ED0', '\u0ED9', state0), new Transition('\u1EA0', '\u1EF9', state0),
				new Transition('\u0CD5', '\u0CD6', state0), new Transition('\u113C', '\u113C', state0),
				new Transition('\u0C05', '\u0C0C', state0), new Transition('\u0061', '\u007A', state0),
				new Transition('\u30A1', '\u30FA', state0), new Transition('\u0009', '\n', state2),
				new Transition('\u0BA3', '\u0BA4', state0), new Transition('\u20D0', '\u20DC', state0),
				new Transition('\u113E', '\u113E', state0), new Transition('\u01FA', '\u0217', state0),
				new Transition('\u0F99', '\u0FAD', state0), new Transition('\u3041', '\u3094', state0),
				new Transition('\u045E', '\u0481', state0), new Transition('\u1140', '\u1140', state0),
				new Transition('\u0B3C', '\u0B43', state0), new Transition('\r', '\r', state2),
				new Transition('\u0D3E', '\u0D43', state0), new Transition('\u11A8', '\u11A8', state0),
				new Transition('\u0180', '\u01C3', state0), new Transition('\u0C0E', '\u0C10', state0),
				new Transition('\u0A0F', '\u0A10', state0), new Transition('\u0BA8', '\u0BAA', state0),
				new Transition('\u0F3E', '\u0F47', state0), new Transition('\u0CDE', '\u0CDE', state0),
				new Transition('\u11AB', '\u11AB', state0), new Transition('\u0141', '\u0148', state0),
				new Transition('\u093C', '\u094D', state0), new Transition('\u0AE0', '\u0AE0', state0),
				new Transition('\u04D0', '\u04EB', state0), new Transition('\u06D5', '\u06E8', state0),
				new Transition('\u0CE0', '\u0CE1', state0), new Transition('\u0531', '\u0556', state0),
				new Transition('\u09AA', '\u09B0', state0), new Transition('\u0D46', '\u0D48', state0),
				new Transition('\u20E1', '\u20E1', state0), new Transition('\u0B47', '\u0B48', state0),
				new Transition('\u11AE', '\u11AF', state0), new Transition('\u05A3', '\u05B9', state0),
				new Transition('\u1F48', '\u1F4D', state0), new Transition('\u09B2', '\u09B2', state0),
				new Transition('\u0BAE', '\u0BB5', state0), new Transition('\u0D4A', '\u0D4D', state0),
				new Transition('\u114C', '\u114C', state0), new Transition('\u0B4B', '\u0B4D', state0),
				new Transition('\u10D0', '\u10F6', state0), new Transition('\u114E', '\u114E', state0),
				new Transition('\u0FB1', '\u0FB7', state0), new Transition('\u0E81', '\u0E82', state0),
				new Transition('\u0A81', '\u0A83', state0), new Transition('\u0E01', '\u0E2E', state0),
				new Transition('\u0C82', '\u0C83', state0), new Transition('\u1150', '\u1150', state0),
				new Transition('\u00D8', '\u00F6', state0), new Transition('\u0E84', '\u0E84', state0),
				new Transition('\u11B7', '\u11B8', state0), new Transition('\u09B6', '\u09B9', state0),
				new Transition('\u0250', '\u02A8', state0), new Transition('\u0483', '\u0486', state0),
				new Transition('\u0AE6', '\u0AEF', state0), new Transition('\u0BB7', '\u0BB9', state0),
				new Transition('\u0CE6', '\u0CEF', state0), new Transition('\u06EA', '\u06ED', state0),
				new Transition('\u0C12', '\u0C28', state0), new Transition('\u0FB9', '\u0FB9', state0),
				new Transition('\u0951', '\u0954', state0), new Transition('\u0A13', '\u0A28', state0),
				new Transition('\u1FB6', '\u1FBC', state0), new Transition('\u0020', '\u0020', state2),
				new Transition('\u11BA', '\u11BA', state0), new Transition('\u1F50', '\u1F57', state0),
				new Transition('\u0E87', '\u0E88', state0), new Transition('\u1154', '\u1155', state0),
				new Transition('\u0A85', '\u0A8B', state0), new Transition('\u09BC', '\u09BC', state0),
				new Transition('\u05BB', '\u05BD', state0), new Transition('\u0C85', '\u0C8C', state0),
				new Transition('\u03A3', '\u03CE', state0), new Transition('\u0B56', '\u0B57', state0),
				new Transition('\u0E8A', '\u0E8A', state0), new Transition('\u0D57', '\u0D57', state0),
				new Transition('\u1FBE', '\u1FBE', state0), new Transition('\u04EE', '\u04F5', state0),
				new Transition('\u05BF', '\u05BF', state0), new Transition('\u0559', '\u0559', state0),
				new Transition('\u1159', '\u1159', state0), new Transition('\u1F59', '\u1F59', state0),
				new Transition('\u11BC', '\u11C2', state0), new Transition('\u0A8D', '\u0A8D', state0),
				new Transition('\u0E8D', '\u0E8D', state0), new Transition('\u0BBE', '\u0BC2', state0),
				new Transition('\u1F5B', '\u1F5B', state0), new Transition('\u05C1', '\u05C2', state0),
				new Transition('\u09BE', '\u09C4', state0), new Transition('\u06F0', '\u06F9', state0),
				new Transition('\u0C8E', '\u0C90', state0), new Transition('\u0F49', '\u0F69', state0),
				new Transition('\u0B5C', '\u0B5D', state0), new Transition('\u3021', '\u302F', state0),
				new Transition('\u1FC2', '\u1FC4', state0), new Transition('\u0A8F', '\u0A91', state0),
				new Transition('\u1F5D', '\u1F5D', state0), new Transition('\u05C4', '\u05C4', state0),
				new Transition('\u0958', '\u0963', state0), new Transition('\u04F8', '\u04F9', state0),
				new Transition('\u0B5F', '\u0B61', state0), new Transition('\u115F', '\u1161', state0),
				new Transition('\u0360', '\u0361', state0), new Transition('\u0D60', '\u0D61', state0),
				new Transition('\u002D', '\u002E', state0), new Transition('\u0A2A', '\u0A30', state0),
				new Transition('\u0BC6', '\u0BC8', state0), new Transition('\u09C7', '\u09C8', state0),
				new Transition('\uAC00', '\uD7A3', state0), new Transition('\u0C2A', '\u0C33', state0),
				new Transition('\u0E94', '\u0E97', state0), new Transition('\u4E00', '\u9FA5', state0),
				new Transition('\u1163', '\u1163', state0), new Transition('\u0621', '\u063A', state0),
				new Transition('\u1FC6', '\u1FCC', state0), new Transition('\u30FC', '\u30FE', state0),
				new Transition('\u1165', '\u1165', state0), new Transition('\u0BCA', '\u0BCD', state0),
				new Transition('\u09CB', '\u09CD', state0), new Transition('\u0A32', '\u0A33', state0),
				new Transition('\u1100', '\u1100', state0), new Transition('\u3099', '\u309A', state0),
				new Transition('\u3031', '\u3035', state0), new Transition('\u1167', '\u1167', state0),
				new Transition('\u0670', '\u06B7', state0), new Transition('\u040E', '\u044F', state0),
				new Transition('\u0030', '\u0039', state0), new Transition('\u0901', '\u0903', state0),
				new Transition('\u0B01', '\u0B03', state0), new Transition('\u0A35', '\u0A36', state0),
				new Transition('\u0D02', '\u0D03', state0), new Transition('\u1102', '\u1103', state0),
				new Transition('\u1169', '\u1169', state0), new Transition('\u014A', '\u017E', state0),
				new Transition('\u0E30', '\u0E3A', state0), new Transition('\u0E99', '\u0E9F', state0),
				new Transition('\u309D', '\u309E', state0), new Transition('\u0C35', '\u0C39', state0),
				new Transition('\u0966', '\u096F', state0), new Transition('\u0B66', '\u0B6F', state0),
				new Transition('\u0D66', '\u0D6F', state0), new Transition('\u1FD0', '\u1FD3', state0),
				new Transition('\u0A38', '\u0A39', state0), new Transition('\u0C92', '\u0CA8', state0),
				new Transition('\u0A93', '\u0AA8', state0), new Transition('\u1105', '\u1107', state0),
				new Transition('\u03D0', '\u03D6', state0), new Transition('\u003A', '\u003A', state3),
				new Transition('\u116D', '\u116E', state0), new Transition('\u0EA1', '\u0EA3', state0),
				new Transition('\u0A3C', '\u0A3C', state0), new Transition('\u1109', '\u1109', state0),
				new Transition('\u0B05', '\u0B0C', state0), new Transition('\u0D05', '\u0D0C', state0),
				new Transition('\u09D7', '\u09D7', state0), new Transition('\u0BD7', '\u0BD7', state0),
				new Transition('\u1F5F', '\u1F7D', state0), new Transition('\u0EA5', '\u0EA5', state0),
				new Transition('\u110B', '\u110C', state0), new Transition('\u1172', '\u1173', state0),
				new Transition('\u1FD6', '\u1FDB', state0), new Transition('\u1F00', '\u1F15', state0),
				new Transition('\u03DA', '\u03DA', state0), new Transition('\u0A3E', '\u0A42', state0),
				new Transition('\u0EA7', '\u0EA7', state0), new Transition('\u0C3E', '\u0C44', state0),
				new Transition('\u1175', '\u1175', state0), new Transition('\u03DC', '\u03DC', state0),
				new Transition('\u0D0E', '\u0D10', state0), new Transition('\u0B0F', '\u0B10', state0),
				new Transition('\u09DC', '\u09DD', state0), new Transition('\u110E', '\u1112', state0),
				new Transition('\u0561', '\u0586', state0), new Transition('\u0EAA', '\u0EAB', state0),
				new Transition('\u03DE', '\u03DE', state0), new Transition('\u05D0', '\u05EA', state0),
				new Transition('\u03E0', '\u03E0', state0), new Transition('\u0AAA', '\u0AB0', state0),
				new Transition('\u0C46', '\u0C48', state0), new Transition('\u0EAD', '\u0EAE', state0),
				new Transition('\u0A47', '\u0A48', state0), new Transition('\u09DF', '\u09E3', state0),
				new Transition('\u01CD', '\u01F0', state0), new Transition('\u0E40', '\u0E4E', state0),
				new Transition('\u0490', '\u04C4', state0), new Transition('\u0CAA', '\u0CB3', state0),
				new Transition('\u0F71', '\u0F84', state0), new Transition('\u0640', '\u0652', state0),
				new Transition('\u0C4A', '\u0C4D', state0), new Transition('\u0F18', '\u0F19', state0),
				new Transition('\u0A4B', '\u0A4D', state0), new Transition('\u0AB2', '\u0AB3', state0),
				new Transition('\u00F8', '\u0131', state0), new Transition('\u1FE0', '\u1FEC', state0),
				new Transition('\u2180', '\u2182', state0), new Transition('\u1F18', '\u1F1D', state0),
				new Transition('\u0981', '\u0983', state0), new Transition('\u0EB0', '\u0EB9', state0),
				new Transition('\u0B82', '\u0B83', state0), new Transition('\u3105', '\u312C', state0),
				new Transition('\u10A0', '\u10C5', state0), new Transition('\u0041', '\u005A', state0),
				new Transition('\u00B7', '\u00B7', state0), new Transition('\u0AB5', '\u0AB9', state0),
				new Transition('\u0CB5', '\u0CB9', state0), new Transition('\u11EB', '\u11EB', state0),
				new Transition('\u0BE7', '\u0BEF', state0), new Transition('\u0D12', '\u0D28', state0),
				new Transition('\u03E2', '\u03F3', state0), new Transition('\u0B13', '\u0B28', state0),
				new Transition('\u09E6', '\u09F1', state0), new Transition('\u0B85', '\u0B8A', state0),
				new Transition('\u0386', '\u038A', state0), new Transition('\u0E50', '\u0E59', state0),
				new Transition('\u0C55', '\u0C56', state0), new Transition('\u0EBB', '\u0EBD', state0),
				new Transition('\u0F86', '\u0F8B', state0), new Transition('\u06BA', '\u06BE', state0),
				new Transition('\u0985', '\u098C', state0), new Transition('\u11F0', '\u11F0', state0),
				new Transition('\u0905', '\u0939', state0), new Transition('\u0451', '\u045C', state0),
				new Transition('\u05F0', '\u05F2', state0), new Transition('\u02BB', '\u02C1', state0),
				new Transition('\u0F20', '\u0F29', state0), new Transition('\u038C', '\u038C', state0),
				new Transition('\u2126', '\u2126', state0), new Transition('\u1FF2', '\u1FF4', state0),
				new Transition('\u0A59', '\u0A5C', state0), new Transition('\u01F4', '\u01F5', state0),
				new Transition('\u0ABC', '\u0AC5', state0), new Transition('\u0CBE', '\u0CC4', state0),
				new Transition('\u0B8E', '\u0B90', state0), new Transition('\u0EC0', '\u0EC4', state0),
				new Transition('\u098F', '\u0990', state0), new Transition('\u0300', '\u0345', state0),
				new Transition('\u1E00', '\u1E9B', state0), new Transition('\u212A', '\u212B', state0),
				new Transition('\u0A5E', '\u0A5E', state0), new Transition('\u005F', '\u005F', state0),
				new Transition('\u11F9', '\u11F9', state0), new Transition('\u0EC6', '\u0EC6', state0),
				new Transition('\u0F90', '\u0F95', state0), new Transition('\u1FF6', '\u1FFC', state0),
				new Transition('\u0B92', '\u0B95', state0), new Transition('\u0C60', '\u0C61', state0),
				new Transition('\u0B2A', '\u0B30', state0), new Transition('\u0CC6', '\u0CC8', state0),
				new Transition('\u04C7', '\u04C8', state0), new Transition('\u212E', '\u212E', state0),
				new Transition('\u0AC7', '\u0AC9', state0), new Transition('\u06C0', '\u06CE', state0),
				new Transition('\u0F97', '\u0F97', state0), new Transition('\u0EC8', '\u0ECD', state0),
				new Transition('\u04CB', '\u04CC', state0), new Transition('\u0660', '\u0669', state0),
				new Transition('\u0CCA', '\u0CCD', state0), new Transition('\u0ACB', '\u0ACD', state0),
				new Transition('\u0B32', '\u0B33', state0), new Transition('\u038E', '\u03A1', state0),
				new Transition('\u0D2A', '\u0D39', state0));
		state1.alter(false, 0, new Transition('\u0B99', '\u0B9A', state0), new Transition('\u00C0', '\u00D6', state0),
				new Transition('\u0B9C', '\u0B9C', state0), new Transition('\u1F20', '\u1F45', state0),
				new Transition('\u119E', '\u119E', state0), new Transition('\u0B36', '\u0B39', state0),
				new Transition('\u06D0', '\u06D3', state0), new Transition('\u0B9E', '\u0B9F', state0),
				new Transition('\u1F80', '\u1FB4', state0), new Transition('\u0993', '\u09A8', state0),
				new Transition('\u0134', '\u013E', state0), new Transition('\u3007', '\u3007', state0),
				new Transition('\u0401', '\u040C', state0), new Transition('\u0A05', '\u0A0A', state0),
				new Transition('\u06D5', '\u06D5', state0), new Transition('\u1EA0', '\u1EF9', state0),
				new Transition('\u113C', '\u113C', state0), new Transition('\u0C05', '\u0C0C', state0),
				new Transition('\u0009', '\n', state1), new Transition('\u0061', '\u007A', state0),
				new Transition('\u30A1', '\u30FA', state0), new Transition('\u093D', '\u093D', state0),
				new Transition('\u0B3D', '\u0B3D', state0), new Transition('\u0BA3', '\u0BA4', state0),
				new Transition('\u113E', '\u113E', state0), new Transition('\u01FA', '\u0217', state0),
				new Transition('\u3041', '\u3094', state0), new Transition('\u045E', '\u0481', state0),
				new Transition('\u0A72', '\u0A74', state0), new Transition('\u1140', '\u1140', state0),
				new Transition('\r', '\r', state1), new Transition('\u11A8', '\u11A8', state0),
				new Transition('\u0180', '\u01C3', state0), new Transition('\u0C0E', '\u0C10', state0),
				new Transition('\u0A0F', '\u0A10', state0), new Transition('\u0BA8', '\u0BAA', state0),
				new Transition('\u0F40', '\u0F47', state0), new Transition('\u0CDE', '\u0CDE', state0),
				new Transition('\u11AB', '\u11AB', state0), new Transition('\u0141', '\u0148', state0),
				new Transition('\u0AE0', '\u0AE0', state0), new Transition('\u04D0', '\u04EB', state0),
				new Transition('\u0CE0', '\u0CE1', state0), new Transition('\u0531', '\u0556', state0),
				new Transition('\u09AA', '\u09B0', state0), new Transition('\u11AE', '\u11AF', state0),
				new Transition('\u1F48', '\u1F4D', state0), new Transition('\u09B2', '\u09B2', state0),
				new Transition('\u0BAE', '\u0BB5', state0), new Transition('\u06E5', '\u06E6', state0),
				new Transition('\u114C', '\u114C', state0), new Transition('\u10D0', '\u10F6', state0),
				new Transition('\u114E', '\u114E', state0), new Transition('\u0E81', '\u0E82', state0),
				new Transition('\u0E01', '\u0E2E', state0), new Transition('\u1150', '\u1150', state0),
				new Transition('\u00D8', '\u00F6', state0), new Transition('\u0E84', '\u0E84', state0),
				new Transition('\u11B7', '\u11B8', state0), new Transition('\u09B6', '\u09B9', state0),
				new Transition('\u0250', '\u02A8', state0), new Transition('\u0BB7', '\u0BB9', state0),
				new Transition('\u0C12', '\u0C28', state0), new Transition('\u0A13', '\u0A28', state0),
				new Transition('\u0020', '\u0020', state1), new Transition('\u1FB6', '\u1FBC', state0),
				new Transition('\u11BA', '\u11BA', state0), new Transition('\u1F50', '\u1F57', state0),
				new Transition('\u0E87', '\u0E88', state0), new Transition('\u1154', '\u1155', state0),
				new Transition('\u0A85', '\u0A8B', state0), new Transition('\u0C85', '\u0C8C', state0),
				new Transition('\u03A3', '\u03CE', state0), new Transition('\u0E8A', '\u0E8A', state0),
				new Transition('\u1FBE', '\u1FBE', state0), new Transition('\u04EE', '\u04F5', state0),
				new Transition('\u0559', '\u0559', state0), new Transition('\u1159', '\u1159', state0),
				new Transition('\u1F59', '\u1F59', state0), new Transition('\u3021', '\u3029', state0),
				new Transition('\u11BC', '\u11C2', state0), new Transition('\u0A8D', '\u0A8D', state0),
				new Transition('\u0E8D', '\u0E8D', state0), new Transition('\u1F5B', '\u1F5B', state0),
				new Transition('\u0C8E', '\u0C90', state0), new Transition('\u0F49', '\u0F69', state0),
				new Transition('\u0B5C', '\u0B5D', state0), new Transition('\u1FC2', '\u1FC4', state0),
				new Transition('\u0A8F', '\u0A91', state0), new Transition('\u1F5D', '\u1F5D', state0),
				new Transition('\u0958', '\u0961', state0), new Transition('\u04F8', '\u04F9', state0),
				new Transition('\u0B5F', '\u0B61', state0), new Transition('\u115F', '\u1161', state0),
				new Transition('\u0D60', '\u0D61', state0), new Transition('\u0A2A', '\u0A30', state0),
				new Transition('\uAC00', '\uD7A3', state0), new Transition('\u0C2A', '\u0C33', state0),
				new Transition('\u0E94', '\u0E97', state0), new Transition('\u4E00', '\u9FA5', state0),
				new Transition('\u1163', '\u1163', state0), new Transition('\u0621', '\u063A', state0),
				new Transition('\u0E30', '\u0E30', state0), new Transition('\u1FC6', '\u1FCC', state0),
				new Transition('\u1165', '\u1165', state0), new Transition('\u0A32', '\u0A33', state0),
				new Transition('\u0E32', '\u0E33', state0), new Transition('\u1100', '\u1100', state0),
				new Transition('\u1167', '\u1167', state0), new Transition('\u0671', '\u06B7', state0),
				new Transition('\u040E', '\u044F', state0), new Transition('\u0A35', '\u0A36', state0),
				new Transition('\u1102', '\u1103', state0), new Transition('\u1169', '\u1169', state0),
				new Transition('\u014A', '\u017E', state0), new Transition('\u0E99', '\u0E9F', state0),
				new Transition('\u0C35', '\u0C39', state0), new Transition('\u1FD0', '\u1FD3', state0),
				new Transition('\u0A38', '\u0A39', state0), new Transition('\u0C92', '\u0CA8', state0),
				new Transition('\u0A93', '\u0AA8', state0), new Transition('\u1105', '\u1107', state0),
				new Transition('\u03D0', '\u03D6', state0), new Transition('\u116D', '\u116E', state0),
				new Transition('\u0EA1', '\u0EA3', state0), new Transition('\u1109', '\u1109', state0),
				new Transition('\u0B05', '\u0B0C', state0), new Transition('\u0D05', '\u0D0C', state0),
				new Transition('\u1F5F', '\u1F7D', state0), new Transition('\u0EA5', '\u0EA5', state0),
				new Transition('\u110B', '\u110C', state0), new Transition('\u1172', '\u1173', state0),
				new Transition('\u1FD6', '\u1FDB', state0), new Transition('\u1F00', '\u1F15', state0),
				new Transition('\u03DA', '\u03DA', state0), new Transition('\u0EA7', '\u0EA7', state0),
				new Transition('\u1175', '\u1175', state0), new Transition('\u03DC', '\u03DC', state0),
				new Transition('\u0D0E', '\u0D10', state0), new Transition('\u0B0F', '\u0B10', state0),
				new Transition('\u09DC', '\u09DD', state0), new Transition('\u0E40', '\u0E45', state0),
				new Transition('\u110E', '\u1112', state0), new Transition('\u0561', '\u0586', state0),
				new Transition('\u0EAA', '\u0EAB', state0), new Transition('\u03DE', '\u03DE', state0),
				new Transition('\u05D0', '\u05EA', state0), new Transition('\u03E0', '\u03E0', state0),
				new Transition('\u0641', '\u064A', state0), new Transition('\u09DF', '\u09E1', state0),
				new Transition('\u0AAA', '\u0AB0', state0), new Transition('\u0EAD', '\u0EAE', state0),
				new Transition('\u01CD', '\u01F0', state0), new Transition('\u0490', '\u04C4', state0),
				new Transition('\u0CAA', '\u0CB3', state0), new Transition('\u0EB0', '\u0EB0', state0),
				new Transition('\u0AB2', '\u0AB3', state0), new Transition('\u0EB2', '\u0EB3', state0),
				new Transition('\u00F8', '\u0131', state0), new Transition('\u1FE0', '\u1FEC', state0),
				new Transition('\u2180', '\u2182', state0), new Transition('\u1F18', '\u1F1D', state0),
				new Transition('\u3105', '\u312C', state0), new Transition('\u10A0', '\u10C5', state0),
				new Transition('\u0041', '\u005A', state0), new Transition('\u0AB5', '\u0AB9', state0),
				new Transition('\u0CB5', '\u0CB9', state0), new Transition('\u11EB', '\u11EB', state0),
				new Transition('\u0D12', '\u0D28', state0), new Transition('\u03E2', '\u03F3', state0),
				new Transition('\u0386', '\u0386', state0), new Transition('\u0B13', '\u0B28', state0),
				new Transition('\u0B85', '\u0B8A', state0), new Transition('\u0388', '\u038A', state0),
				new Transition('\u06BA', '\u06BE', state0), new Transition('\u0985', '\u098C', state0),
				new Transition('\u11F0', '\u11F0', state0), new Transition('\u0ABD', '\u0ABD', state0),
				new Transition('\u0EBD', '\u0EBD', state0), new Transition('\u09F0', '\u09F1', state0),
				new Transition('\u0905', '\u0939', state0), new Transition('\u0451', '\u045C', state0),
				new Transition('\u05F0', '\u05F2', state0), new Transition('\u02BB', '\u02C1', state0),
				new Transition('\u038C', '\u038C', state0), new Transition('\u2126', '\u2126', state0),
				new Transition('\u1FF2', '\u1FF4', state0), new Transition('\u0A59', '\u0A5C', state0),
				new Transition('\u01F4', '\u01F5', state0), new Transition('\u0B8E', '\u0B90', state0),
				new Transition('\u0EC0', '\u0EC4', state0), new Transition('\u098F', '\u0990', state0),
				new Transition('\u1E00', '\u1E9B', state0), new Transition('\u212A', '\u212B', state0),
				new Transition('\u0A5E', '\u0A5E', state0), new Transition('\u005F', '\u005F', state0),
				new Transition('\u11F9', '\u11F9', state0), new Transition('\u1FF6', '\u1FFC', state0),
				new Transition('\u0B92', '\u0B95', state0), new Transition('\u0C60', '\u0C61', state0),
				new Transition('\u0B2A', '\u0B30', state0), new Transition('\u04C7', '\u04C8', state0),
				new Transition('\u212E', '\u212E', state0), new Transition('\u06C0', '\u06CE', state0),
				new Transition('\u04CB', '\u04CC', state0), new Transition('\u0B32', '\u0B33', state0),
				new Transition('\u038E', '\u03A1', state0), new Transition('\u0D2A', '\u0D39', state0));
		state2.alter(true, 2, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		state3.alter(false, 4, new Transition('\u0B99', '\u0B9A', state4), new Transition('\u00C0', '\u00D6', state4),
				new Transition('\u0B9C', '\u0B9C', state4), new Transition('\u1F20', '\u1F45', state4),
				new Transition('\u119E', '\u119E', state4), new Transition('\u0B36', '\u0B39', state4),
				new Transition('\u06D0', '\u06D3', state4), new Transition('\u0B9E', '\u0B9F', state4),
				new Transition('\u1F80', '\u1FB4', state4), new Transition('\u0993', '\u09A8', state4),
				new Transition('\u0134', '\u013E', state4), new Transition('\u3007', '\u3007', state4),
				new Transition('\u0401', '\u040C', state4), new Transition('\u0A05', '\u0A0A', state4),
				new Transition('\u06D5', '\u06D5', state4), new Transition('\u1EA0', '\u1EF9', state4),
				new Transition('\u113C', '\u113C', state4), new Transition('\u0C05', '\u0C0C', state4),
				new Transition('\u0061', '\u007A', state4), new Transition('\u30A1', '\u30FA', state4),
				new Transition('\u093D', '\u093D', state4), new Transition('\u0B3D', '\u0B3D', state4),
				new Transition('\u0BA3', '\u0BA4', state4), new Transition('\u113E', '\u113E', state4),
				new Transition('\u01FA', '\u0217', state4), new Transition('\u3041', '\u3094', state4),
				new Transition('\u045E', '\u0481', state4), new Transition('\u0A72', '\u0A74', state4),
				new Transition('\u1140', '\u1140', state4), new Transition('\u11A8', '\u11A8', state4),
				new Transition('\u0180', '\u01C3', state4), new Transition('\u0C0E', '\u0C10', state4),
				new Transition('\u0A0F', '\u0A10', state4), new Transition('\u0BA8', '\u0BAA', state4),
				new Transition('\u0F40', '\u0F47', state4), new Transition('\u0CDE', '\u0CDE', state4),
				new Transition('\u11AB', '\u11AB', state4), new Transition('\u0141', '\u0148', state4),
				new Transition('\u0AE0', '\u0AE0', state4), new Transition('\u04D0', '\u04EB', state4),
				new Transition('\u0CE0', '\u0CE1', state4), new Transition('\u0531', '\u0556', state4),
				new Transition('\u09AA', '\u09B0', state4), new Transition('\u11AE', '\u11AF', state4),
				new Transition('\u1F48', '\u1F4D', state4), new Transition('\u09B2', '\u09B2', state4),
				new Transition('\u0BAE', '\u0BB5', state4), new Transition('\u06E5', '\u06E6', state4),
				new Transition('\u114C', '\u114C', state4), new Transition('\u10D0', '\u10F6', state4),
				new Transition('\u114E', '\u114E', state4), new Transition('\u0E81', '\u0E82', state4),
				new Transition('\u0E01', '\u0E2E', state4), new Transition('\u1150', '\u1150', state4),
				new Transition('\u00D8', '\u00F6', state4), new Transition('\u0E84', '\u0E84', state4),
				new Transition('\u11B7', '\u11B8', state4), new Transition('\u09B6', '\u09B9', state4),
				new Transition('\u0250', '\u02A8', state4), new Transition('\u0BB7', '\u0BB9', state4),
				new Transition('\u0C12', '\u0C28', state4), new Transition('\u0A13', '\u0A28', state4),
				new Transition('\u1FB6', '\u1FBC', state4), new Transition('\u11BA', '\u11BA', state4),
				new Transition('\u1F50', '\u1F57', state4), new Transition('\u0E87', '\u0E88', state4),
				new Transition('\u1154', '\u1155', state4), new Transition('\u0A85', '\u0A8B', state4),
				new Transition('\u0C85', '\u0C8C', state4), new Transition('\u03A3', '\u03CE', state4),
				new Transition('\u0E8A', '\u0E8A', state4), new Transition('\u1FBE', '\u1FBE', state4),
				new Transition('\u04EE', '\u04F5', state4), new Transition('\u0559', '\u0559', state4),
				new Transition('\u1159', '\u1159', state4), new Transition('\u1F59', '\u1F59', state4),
				new Transition('\u3021', '\u3029', state4), new Transition('\u11BC', '\u11C2', state4),
				new Transition('\u0A8D', '\u0A8D', state4), new Transition('\u0E8D', '\u0E8D', state4),
				new Transition('\u1F5B', '\u1F5B', state4), new Transition('\u0C8E', '\u0C90', state4),
				new Transition('\u0F49', '\u0F69', state4), new Transition('\u0B5C', '\u0B5D', state4),
				new Transition('\u1FC2', '\u1FC4', state4), new Transition('\u0A8F', '\u0A91', state4),
				new Transition('\u1F5D', '\u1F5D', state4), new Transition('\u0958', '\u0961', state4),
				new Transition('\u04F8', '\u04F9', state4), new Transition('\u0B5F', '\u0B61', state4),
				new Transition('\u115F', '\u1161', state4), new Transition('\u0D60', '\u0D61', state4),
				new Transition('\u0A2A', '\u0A30', state4), new Transition('\uAC00', '\uD7A3', state4),
				new Transition('\u0C2A', '\u0C33', state4), new Transition('\u0E94', '\u0E97', state4),
				new Transition('\u4E00', '\u9FA5', state4), new Transition('\u1163', '\u1163', state4),
				new Transition('\u0621', '\u063A', state4), new Transition('\u0E30', '\u0E30', state4),
				new Transition('\u1FC6', '\u1FCC', state4), new Transition('\u1165', '\u1165', state4),
				new Transition('\u0A32', '\u0A33', state4), new Transition('\u0E32', '\u0E33', state4),
				new Transition('\u1100', '\u1100', state4), new Transition('\u1167', '\u1167', state4),
				new Transition('\u0671', '\u06B7', state4), new Transition('\u040E', '\u044F', state4),
				new Transition('\u0A35', '\u0A36', state4), new Transition('\u1102', '\u1103', state4),
				new Transition('\u1169', '\u1169', state4), new Transition('\u014A', '\u017E', state4),
				new Transition('\u0E99', '\u0E9F', state4), new Transition('\u0C35', '\u0C39', state4),
				new Transition('\u1FD0', '\u1FD3', state4), new Transition('\u0A38', '\u0A39', state4),
				new Transition('\u0C92', '\u0CA8', state4), new Transition('\u0A93', '\u0AA8', state4),
				new Transition('\u1105', '\u1107', state4), new Transition('\u03D0', '\u03D6', state4),
				new Transition('\u116D', '\u116E', state4), new Transition('\u0EA1', '\u0EA3', state4),
				new Transition('\u1109', '\u1109', state4), new Transition('\u0B05', '\u0B0C', state4),
				new Transition('\u0D05', '\u0D0C', state4), new Transition('\u1F5F', '\u1F7D', state4),
				new Transition('\u0EA5', '\u0EA5', state4), new Transition('\u110B', '\u110C', state4),
				new Transition('\u1172', '\u1173', state4), new Transition('\u1FD6', '\u1FDB', state4),
				new Transition('\u1F00', '\u1F15', state4), new Transition('\u03DA', '\u03DA', state4),
				new Transition('\u0EA7', '\u0EA7', state4), new Transition('\u1175', '\u1175', state4),
				new Transition('\u03DC', '\u03DC', state4), new Transition('\u0D0E', '\u0D10', state4),
				new Transition('\u0B0F', '\u0B10', state4), new Transition('\u09DC', '\u09DD', state4),
				new Transition('\u0E40', '\u0E45', state4), new Transition('\u110E', '\u1112', state4),
				new Transition('\u0561', '\u0586', state4), new Transition('\u0EAA', '\u0EAB', state4),
				new Transition('\u03DE', '\u03DE', state4), new Transition('\u05D0', '\u05EA', state4),
				new Transition('\u03E0', '\u03E0', state4), new Transition('\u0641', '\u064A', state4),
				new Transition('\u09DF', '\u09E1', state4), new Transition('\u0AAA', '\u0AB0', state4),
				new Transition('\u0EAD', '\u0EAE', state4), new Transition('\u01CD', '\u01F0', state4),
				new Transition('\u0490', '\u04C4', state4), new Transition('\u0CAA', '\u0CB3', state4),
				new Transition('\u0EB0', '\u0EB0', state4), new Transition('\u0AB2', '\u0AB3', state4),
				new Transition('\u0EB2', '\u0EB3', state4), new Transition('\u00F8', '\u0131', state4),
				new Transition('\u1FE0', '\u1FEC', state4), new Transition('\u2180', '\u2182', state4),
				new Transition('\u1F18', '\u1F1D', state4), new Transition('\u3105', '\u312C', state4),
				new Transition('\u10A0', '\u10C5', state4), new Transition('\u0041', '\u005A', state4),
				new Transition('\u0AB5', '\u0AB9', state4), new Transition('\u0CB5', '\u0CB9', state4),
				new Transition('\u11EB', '\u11EB', state4), new Transition('\u0D12', '\u0D28', state4),
				new Transition('\u03E2', '\u03F3', state4), new Transition('\u0386', '\u0386', state4),
				new Transition('\u0B13', '\u0B28', state4), new Transition('\u0B85', '\u0B8A', state4),
				new Transition('\u0388', '\u038A', state4), new Transition('\u06BA', '\u06BE', state4),
				new Transition('\u0985', '\u098C', state4), new Transition('\u11F0', '\u11F0', state4),
				new Transition('\u0ABD', '\u0ABD', state4), new Transition('\u0EBD', '\u0EBD', state4),
				new Transition('\u09F0', '\u09F1', state4), new Transition('\u0905', '\u0939', state4),
				new Transition('\u0451', '\u045C', state4), new Transition('\u05F0', '\u05F2', state4),
				new Transition('\u02BB', '\u02C1', state4), new Transition('\u038C', '\u038C', state4),
				new Transition('\u2126', '\u2126', state4), new Transition('\u1FF2', '\u1FF4', state4),
				new Transition('\u0A59', '\u0A5C', state4), new Transition('\u01F4', '\u01F5', state4),
				new Transition('\u0B8E', '\u0B90', state4), new Transition('\u0EC0', '\u0EC4', state4),
				new Transition('\u098F', '\u0990', state4), new Transition('\u1E00', '\u1E9B', state4),
				new Transition('\u212A', '\u212B', state4), new Transition('\u0A5E', '\u0A5E', state4),
				new Transition('\u005F', '\u005F', state4), new Transition('\u11F9', '\u11F9', state4),
				new Transition('\u1FF6', '\u1FFC', state4), new Transition('\u0B92', '\u0B95', state4),
				new Transition('\u0C60', '\u0C61', state4), new Transition('\u0B2A', '\u0B30', state4),
				new Transition('\u04C7', '\u04C8', state4), new Transition('\u212E', '\u212E', state4),
				new Transition('\u06C0', '\u06CE', state4), new Transition('\u04CB', '\u04CC', state4),
				new Transition('\u0B32', '\u0B33', state4), new Transition('\u038E', '\u03A1', state4),
				new Transition('\u0D2A', '\u0D39', state4));
		state4.alter(true, 3, new Transition('\u0B99', '\u0B9A', state4), new Transition('\u00C0', '\u00D6', state4),
				new Transition('\u0591', '\u05A1', state4), new Transition('\u0F35', '\u0F35', state4),
				new Transition('\u0A02', '\u0A02', state4), new Transition('\u0C01', '\u0C03', state4),
				new Transition('\u0B9C', '\u0B9C', state4), new Transition('\u1F20', '\u1F45', state4),
				new Transition('\u02D0', '\u02D1', state4), new Transition('\u0F37', '\u0F37', state4),
				new Transition('\u119E', '\u119E', state4), new Transition('\u0B36', '\u0B39', state4),
				new Transition('\u06D0', '\u06D3', state4), new Transition('\u0B9E', '\u0B9F', state4),
				new Transition('\u0C66', '\u0C6F', state4), new Transition('\u3005', '\u3005', state4),
				new Transition('\u1F80', '\u1FB4', state4), new Transition('\u0F39', '\u0F39', state4),
				new Transition('\u0993', '\u09A8', state4), new Transition('\u0134', '\u013E', state4),
				new Transition('\u3007', '\u3007', state4), new Transition('\u0401', '\u040C', state4),
				new Transition('\u0A05', '\u0A0A', state4), new Transition('\u0A66', '\u0A74', state4),
				new Transition('\u0ED0', '\u0ED9', state4), new Transition('\u1EA0', '\u1EF9', state4),
				new Transition('\u0CD5', '\u0CD6', state4), new Transition('\u113C', '\u113C', state4),
				new Transition('\u0C05', '\u0C0C', state4), new Transition('\u0009', '\n', state2),
				new Transition('\u0061', '\u007A', state4), new Transition('\u30A1', '\u30FA', state4),
				new Transition('\u0BA3', '\u0BA4', state4), new Transition('\u20D0', '\u20DC', state4),
				new Transition('\u113E', '\u113E', state4), new Transition('\u01FA', '\u0217', state4),
				new Transition('\u0F99', '\u0FAD', state4), new Transition('\u3041', '\u3094', state4),
				new Transition('\u045E', '\u0481', state4), new Transition('\u1140', '\u1140', state4),
				new Transition('\r', '\r', state2), new Transition('\u0B3C', '\u0B43', state4),
				new Transition('\u0D3E', '\u0D43', state4), new Transition('\u11A8', '\u11A8', state4),
				new Transition('\u0180', '\u01C3', state4), new Transition('\u0C0E', '\u0C10', state4),
				new Transition('\u0A0F', '\u0A10', state4), new Transition('\u0BA8', '\u0BAA', state4),
				new Transition('\u0F3E', '\u0F47', state4), new Transition('\u0CDE', '\u0CDE', state4),
				new Transition('\u11AB', '\u11AB', state4), new Transition('\u0141', '\u0148', state4),
				new Transition('\u093C', '\u094D', state4), new Transition('\u0AE0', '\u0AE0', state4),
				new Transition('\u04D0', '\u04EB', state4), new Transition('\u06D5', '\u06E8', state4),
				new Transition('\u0CE0', '\u0CE1', state4), new Transition('\u0531', '\u0556', state4),
				new Transition('\u09AA', '\u09B0', state4), new Transition('\u0D46', '\u0D48', state4),
				new Transition('\u20E1', '\u20E1', state4), new Transition('\u0B47', '\u0B48', state4),
				new Transition('\u11AE', '\u11AF', state4), new Transition('\u05A3', '\u05B9', state4),
				new Transition('\u1F48', '\u1F4D', state4), new Transition('\u09B2', '\u09B2', state4),
				new Transition('\u0BAE', '\u0BB5', state4), new Transition('\u0D4A', '\u0D4D', state4),
				new Transition('\u114C', '\u114C', state4), new Transition('\u0B4B', '\u0B4D', state4),
				new Transition('\u10D0', '\u10F6', state4), new Transition('\u114E', '\u114E', state4),
				new Transition('\u0FB1', '\u0FB7', state4), new Transition('\u0E81', '\u0E82', state4),
				new Transition('\u0A81', '\u0A83', state4), new Transition('\u0E01', '\u0E2E', state4),
				new Transition('\u0C82', '\u0C83', state4), new Transition('\u1150', '\u1150', state4),
				new Transition('\u00D8', '\u00F6', state4), new Transition('\u0E84', '\u0E84', state4),
				new Transition('\u11B7', '\u11B8', state4), new Transition('\u09B6', '\u09B9', state4),
				new Transition('\u0250', '\u02A8', state4), new Transition('\u0483', '\u0486', state4),
				new Transition('\u0AE6', '\u0AEF', state4), new Transition('\u0BB7', '\u0BB9', state4),
				new Transition('\u0CE6', '\u0CEF', state4), new Transition('\u06EA', '\u06ED', state4),
				new Transition('\u0C12', '\u0C28', state4), new Transition('\u0FB9', '\u0FB9', state4),
				new Transition('\u0951', '\u0954', state4), new Transition('\u0A13', '\u0A28', state4),
				new Transition('\u0020', '\u0020', state2), new Transition('\u1FB6', '\u1FBC', state4),
				new Transition('\u11BA', '\u11BA', state4), new Transition('\u1F50', '\u1F57', state4),
				new Transition('\u0E87', '\u0E88', state4), new Transition('\u1154', '\u1155', state4),
				new Transition('\u0A85', '\u0A8B', state4), new Transition('\u09BC', '\u09BC', state4),
				new Transition('\u05BB', '\u05BD', state4), new Transition('\u0C85', '\u0C8C', state4),
				new Transition('\u03A3', '\u03CE', state4), new Transition('\u0B56', '\u0B57', state4),
				new Transition('\u0E8A', '\u0E8A', state4), new Transition('\u0D57', '\u0D57', state4),
				new Transition('\u1FBE', '\u1FBE', state4), new Transition('\u04EE', '\u04F5', state4),
				new Transition('\u05BF', '\u05BF', state4), new Transition('\u0559', '\u0559', state4),
				new Transition('\u1159', '\u1159', state4), new Transition('\u1F59', '\u1F59', state4),
				new Transition('\u11BC', '\u11C2', state4), new Transition('\u0A8D', '\u0A8D', state4),
				new Transition('\u0E8D', '\u0E8D', state4), new Transition('\u0BBE', '\u0BC2', state4),
				new Transition('\u1F5B', '\u1F5B', state4), new Transition('\u05C1', '\u05C2', state4),
				new Transition('\u09BE', '\u09C4', state4), new Transition('\u06F0', '\u06F9', state4),
				new Transition('\u0C8E', '\u0C90', state4), new Transition('\u0F49', '\u0F69', state4),
				new Transition('\u0B5C', '\u0B5D', state4), new Transition('\u3021', '\u302F', state4),
				new Transition('\u1FC2', '\u1FC4', state4), new Transition('\u0A8F', '\u0A91', state4),
				new Transition('\u1F5D', '\u1F5D', state4), new Transition('\u05C4', '\u05C4', state4),
				new Transition('\u0958', '\u0963', state4), new Transition('\u04F8', '\u04F9', state4),
				new Transition('\u0B5F', '\u0B61', state4), new Transition('\u115F', '\u1161', state4),
				new Transition('\u0360', '\u0361', state4), new Transition('\u0D60', '\u0D61', state4),
				new Transition('\u002D', '\u002E', state4), new Transition('\u0A2A', '\u0A30', state4),
				new Transition('\u0BC6', '\u0BC8', state4), new Transition('\u09C7', '\u09C8', state4),
				new Transition('\uAC00', '\uD7A3', state4), new Transition('\u0C2A', '\u0C33', state4),
				new Transition('\u0E94', '\u0E97', state4), new Transition('\u4E00', '\u9FA5', state4),
				new Transition('\u1163', '\u1163', state4), new Transition('\u0621', '\u063A', state4),
				new Transition('\u1FC6', '\u1FCC', state4), new Transition('\u30FC', '\u30FE', state4),
				new Transition('\u1165', '\u1165', state4), new Transition('\u0BCA', '\u0BCD', state4),
				new Transition('\u09CB', '\u09CD', state4), new Transition('\u0A32', '\u0A33', state4),
				new Transition('\u1100', '\u1100', state4), new Transition('\u3099', '\u309A', state4),
				new Transition('\u3031', '\u3035', state4), new Transition('\u1167', '\u1167', state4),
				new Transition('\u0670', '\u06B7', state4), new Transition('\u040E', '\u044F', state4),
				new Transition('\u0030', '\u0039', state4), new Transition('\u0901', '\u0903', state4),
				new Transition('\u0B01', '\u0B03', state4), new Transition('\u0A35', '\u0A36', state4),
				new Transition('\u0D02', '\u0D03', state4), new Transition('\u1102', '\u1103', state4),
				new Transition('\u1169', '\u1169', state4), new Transition('\u014A', '\u017E', state4),
				new Transition('\u0E30', '\u0E3A', state4), new Transition('\u0E99', '\u0E9F', state4),
				new Transition('\u309D', '\u309E', state4), new Transition('\u0C35', '\u0C39', state4),
				new Transition('\u0966', '\u096F', state4), new Transition('\u0B66', '\u0B6F', state4),
				new Transition('\u0D66', '\u0D6F', state4), new Transition('\u1FD0', '\u1FD3', state4),
				new Transition('\u0A38', '\u0A39', state4), new Transition('\u0C92', '\u0CA8', state4),
				new Transition('\u0A93', '\u0AA8', state4), new Transition('\u1105', '\u1107', state4),
				new Transition('\u03D0', '\u03D6', state4), new Transition('\u116D', '\u116E', state4),
				new Transition('\u0EA1', '\u0EA3', state4), new Transition('\u0A3C', '\u0A3C', state4),
				new Transition('\u1109', '\u1109', state4), new Transition('\u0B05', '\u0B0C', state4),
				new Transition('\u0D05', '\u0D0C', state4), new Transition('\u09D7', '\u09D7', state4),
				new Transition('\u0BD7', '\u0BD7', state4), new Transition('\u1F5F', '\u1F7D', state4),
				new Transition('\u0EA5', '\u0EA5', state4), new Transition('\u110B', '\u110C', state4),
				new Transition('\u1172', '\u1173', state4), new Transition('\u1FD6', '\u1FDB', state4),
				new Transition('\u1F00', '\u1F15', state4), new Transition('\u03DA', '\u03DA', state4),
				new Transition('\u0A3E', '\u0A42', state4), new Transition('\u0EA7', '\u0EA7', state4),
				new Transition('\u0C3E', '\u0C44', state4), new Transition('\u1175', '\u1175', state4),
				new Transition('\u03DC', '\u03DC', state4), new Transition('\u0D0E', '\u0D10', state4),
				new Transition('\u0B0F', '\u0B10', state4), new Transition('\u09DC', '\u09DD', state4),
				new Transition('\u110E', '\u1112', state4), new Transition('\u0561', '\u0586', state4),
				new Transition('\u0EAA', '\u0EAB', state4), new Transition('\u03DE', '\u03DE', state4),
				new Transition('\u05D0', '\u05EA', state4), new Transition('\u03E0', '\u03E0', state4),
				new Transition('\u0AAA', '\u0AB0', state4), new Transition('\u0C46', '\u0C48', state4),
				new Transition('\u0EAD', '\u0EAE', state4), new Transition('\u0A47', '\u0A48', state4),
				new Transition('\u09DF', '\u09E3', state4), new Transition('\u01CD', '\u01F0', state4),
				new Transition('\u0E40', '\u0E4E', state4), new Transition('\u0490', '\u04C4', state4),
				new Transition('\u0CAA', '\u0CB3', state4), new Transition('\u0F71', '\u0F84', state4),
				new Transition('\u0640', '\u0652', state4), new Transition('\u0C4A', '\u0C4D', state4),
				new Transition('\u0F18', '\u0F19', state4), new Transition('\u0A4B', '\u0A4D', state4),
				new Transition('\u0AB2', '\u0AB3', state4), new Transition('\u00F8', '\u0131', state4),
				new Transition('\u1FE0', '\u1FEC', state4), new Transition('\u2180', '\u2182', state4),
				new Transition('\u1F18', '\u1F1D', state4), new Transition('\u0981', '\u0983', state4),
				new Transition('\u0EB0', '\u0EB9', state4), new Transition('\u0B82', '\u0B83', state4),
				new Transition('\u3105', '\u312C', state4), new Transition('\u10A0', '\u10C5', state4),
				new Transition('\u0041', '\u005A', state4), new Transition('\u00B7', '\u00B7', state4),
				new Transition('\u0AB5', '\u0AB9', state4), new Transition('\u0CB5', '\u0CB9', state4),
				new Transition('\u11EB', '\u11EB', state4), new Transition('\u0BE7', '\u0BEF', state4),
				new Transition('\u0D12', '\u0D28', state4), new Transition('\u03E2', '\u03F3', state4),
				new Transition('\u0B13', '\u0B28', state4), new Transition('\u09E6', '\u09F1', state4),
				new Transition('\u0B85', '\u0B8A', state4), new Transition('\u0386', '\u038A', state4),
				new Transition('\u0E50', '\u0E59', state4), new Transition('\u0C55', '\u0C56', state4),
				new Transition('\u0EBB', '\u0EBD', state4), new Transition('\u0F86', '\u0F8B', state4),
				new Transition('\u06BA', '\u06BE', state4), new Transition('\u0985', '\u098C', state4),
				new Transition('\u11F0', '\u11F0', state4), new Transition('\u0905', '\u0939', state4),
				new Transition('\u0451', '\u045C', state4), new Transition('\u05F0', '\u05F2', state4),
				new Transition('\u02BB', '\u02C1', state4), new Transition('\u0F20', '\u0F29', state4),
				new Transition('\u038C', '\u038C', state4), new Transition('\u2126', '\u2126', state4),
				new Transition('\u1FF2', '\u1FF4', state4), new Transition('\u0A59', '\u0A5C', state4),
				new Transition('\u01F4', '\u01F5', state4), new Transition('\u0ABC', '\u0AC5', state4),
				new Transition('\u0CBE', '\u0CC4', state4), new Transition('\u0B8E', '\u0B90', state4),
				new Transition('\u0EC0', '\u0EC4', state4), new Transition('\u098F', '\u0990', state4),
				new Transition('\u0300', '\u0345', state4), new Transition('\u1E00', '\u1E9B', state4),
				new Transition('\u212A', '\u212B', state4), new Transition('\u0A5E', '\u0A5E', state4),
				new Transition('\u005F', '\u005F', state4), new Transition('\u11F9', '\u11F9', state4),
				new Transition('\u0EC6', '\u0EC6', state4), new Transition('\u0F90', '\u0F95', state4),
				new Transition('\u1FF6', '\u1FFC', state4), new Transition('\u0B92', '\u0B95', state4),
				new Transition('\u0C60', '\u0C61', state4), new Transition('\u0B2A', '\u0B30', state4),
				new Transition('\u0CC6', '\u0CC8', state4), new Transition('\u04C7', '\u04C8', state4),
				new Transition('\u212E', '\u212E', state4), new Transition('\u0AC7', '\u0AC9', state4),
				new Transition('\u06C0', '\u06CE', state4), new Transition('\u0F97', '\u0F97', state4),
				new Transition('\u0EC8', '\u0ECD', state4), new Transition('\u04CB', '\u04CC', state4),
				new Transition('\u0660', '\u0669', state4), new Transition('\u0CCA', '\u0CCD', state4),
				new Transition('\u0ACB', '\u0ACD', state4), new Transition('\u0B32', '\u0B33', state4),
				new Transition('\u038E', '\u03A1', state4), new Transition('\u0D2A', '\u0D39', state4));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut118() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u16A0', '\u16FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut119() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 2, new Transition('\uDEC1', '\uDEC1', state3), new Transition('\uDEDB', '\uDEDB', state3),
				new Transition('\uDEFB', '\uDEFB', state3), new Transition('\uDF89', '\uDF89', state3),
				new Transition('\uDFA9', '\uDFA9', state3), new Transition('\uDFC3', '\uDFC3', state3),
				new Transition('\uDF15', '\uDF15', state3), new Transition('\uDF35', '\uDF35', state3),
				new Transition('\uDF4F', '\uDF4F', state3), new Transition('\uDF6F', '\uDF6F', state3));
		state1.alter(false, 1, new Transition('\uDD2A', '\uDD64', state3), new Transition('\uDD00', '\uDD26', state3),
				new Transition('\uDD83', '\uDD84', state3), new Transition('\uDD8C', '\uDDA9', state3),
				new Transition('\uDDAE', '\uDDDD', state3), new Transition('\uDC00', '\uDCF5', state3),
				new Transition('\uDD6A', '\uDD6C', state3));
		state2.alter(false, 3, new Transition('\uA490', '\uA4A1', state3), new Transition('\uD834', '\uD834', state1),
				new Transition('\u2100', '\u2101', state3), new Transition('\u0F34', '\u0F34', state3),
				new Transition('\u1FCD', '\u1FCF', state3), new Transition('\u3196', '\u319F', state3),
				new Transition('\uFE69', '\uFE69', state3), new Transition('\u309B', '\u309C', state3),
				new Transition('\u0F01', '\u0F03', state3), new Transition('\u0FCF', '\u0FCF', state3),
				new Transition('\uD835', '\uD835', state0), new Transition('\u0F36', '\u0F36', state3),
				new Transition('\u2701', '\u2704', state3), new Transition('\u2E9B', '\u2EF3', state3),
				new Transition('\uFF04', '\uFF04', state3), new Transition('\u3036', '\u3037', state3),
				new Transition('\u3004', '\u3004', state3), new Transition('\u0F38', '\u0F38', state3),
				new Transition('\u2103', '\u2106', state3), new Transition('\u322A', '\u3243', state3),
				new Transition('\u213A', '\u213A', state3), new Transition('\u328A', '\u32B0', state3),
				new Transition('\u25A0', '\u25F7', state3), new Transition('\u2706', '\u2709', state3),
				new Transition('\u2108', '\u2109', state3), new Transition('\u0B70', '\u0B70', state3),
				new Transition('\u3260', '\u327B', state3), new Transition('\u003C', '\u003E', state3),
				new Transition('\uFF3E', '\uFF3E', state3), new Transition('\u00D7', '\u00D7', state3),
				new Transition('\u2729', '\u274B', state3), new Transition('\uFF0B', '\uFF0B', state3),
				new Transition('\u2600', '\u2613', state3), new Transition('\u303E', '\u303F', state3),
				new Transition('\u0E3F', '\u0E3F', state3), new Transition('\u2798', '\u27AF', state3),
				new Transition('\u00A2', '\u00A9', state3), new Transition('\u02D2', '\u02DF', state3),
				new Transition('\uFF40', '\uFF40', state3), new Transition('\u0374', '\u0375', state3),
				new Transition('\u17DB', '\u17DB', state3), new Transition('\u20A0', '\u20AF', state3),
				new Transition('\u2044', '\u2044', state3), new Transition('\u3200', '\u321C', state3),
				new Transition('\u1FDD', '\u1FDF', state3), new Transition('\u00AC', '\u00AC', state3),
				new Transition('\u3012', '\u3013', state3), new Transition('\u2440', '\u244A', state3),
				new Transition('\uA4A4', '\uA4B3', state3), new Transition('\u3300', '\u3376', state3),
				new Transition('\u2114', '\u2114', state3), new Transition('\u207A', '\u207C', state3),
				new Transition('\u0F13', '\u0F17', state3), new Transition('\u007C', '\u007C', state3),
				new Transition('\u00AE', '\u00B1', state3), new Transition('\u2400', '\u2426', state3),
				new Transition('\u2116', '\u2118', state3), new Transition('\u007E', '\u007E', state3),
				new Transition('\uFFE0', '\uFFE6', state3), new Transition('\u2300', '\u2328', state3),
				new Transition('\u327F', '\u327F', state3), new Transition('\u2F00', '\u2FD5', state3),
				new Transition('\u274D', '\u274D', state3), new Transition('\u00B4', '\u00B4', state3),
				new Transition('\u2619', '\u2671', state3), new Transition('\u0482', '\u0482', state3),
				new Transition('\u337B', '\u33DD', state3), new Transition('\u06E9', '\u06E9', state3),
				new Transition('\u270C', '\u2727', state3), new Transition('\u00B6', '\u00B6', state3),
				new Transition('\u02E5', '\u02ED', state3), new Transition('\u0F1A', '\u0F1F', state3),
				new Transition('\u274F', '\u2752', state3), new Transition('\uFF1C', '\uFF1E', state3),
				new Transition('\u0384', '\u0385', state3), new Transition('\u00B8', '\u00B8', state3),
				new Transition('\u32D0', '\u32FE', state3), new Transition('\u27B1', '\u27BE', state3),
				new Transition('\uFFE8', '\uFFEE', state3), new Transition('\u02B9', '\u02BA', state3),
				new Transition('\u3020', '\u3020', state3), new Transition('\u211E', '\u2123', state3),
				new Transition('\u1FED', '\u1FEF', state3), new Transition('\uA4B5', '\uA4C0', state3),
				new Transition('\u2756', '\u2756', state3), new Transition('\u1FBD', '\u1FBD', state3),
				new Transition('\u0024', '\u0024', state3), new Transition('\u208A', '\u208C', state3),
				new Transition('\u2125', '\u2125', state3), new Transition('\u33E0', '\u33FE', state3),
				new Transition('\u09F2', '\u09F3', state3), new Transition('\u2500', '\u2595', state3),
				new Transition('\u1FBF', '\u1FC1', state3), new Transition('\u2127', '\u2127', state3),
				new Transition('\u232B', '\u237B', state3), new Transition('\u237D', '\u239A', state3),
				new Transition('\uFF5C', '\uFF5C', state3), new Transition('\uFB29', '\uFB29', state3),
				new Transition('\u2758', '\u275E', state3), new Transition('\u0FBE', '\u0FC5', state3),
				new Transition('\u2E80', '\u2E99', state3), new Transition('\u2129', '\u2129', state3),
				new Transition('\u2FF0', '\u2FFB', state3), new Transition('\uFF5E', '\uFF5E', state3),
				new Transition('\u00F7', '\u00F7', state3), new Transition('\u2200', '\u22F1', state3),
				new Transition('\u3190', '\u3191', state3), new Transition('\uA4C2', '\uA4C4', state3),
				new Transition('\u005E', '\u005E', state3), new Transition('\u002B', '\u002B', state3),
				new Transition('\uA4C6', '\uA4C6', state3), new Transition('\u0060', '\u0060', state3),
				new Transition('\u32C0', '\u32CB', state3), new Transition('\u09FA', '\u09FA', state3),
				new Transition('\u2794', '\u2794', state3), new Transition('\u212E', '\u212E', state3),
				new Transition('\uFFFC', '\uFFFD', state3), new Transition('\uFE62', '\uFE62', state3),
				new Transition('\u02C2', '\u02CF', state3), new Transition('\u0FC7', '\u0FCC', state3),
				new Transition('\u249C', '\u24E9', state3), new Transition('\u06FD', '\u06FE', state3),
				new Transition('\u1FFD', '\u1FFE', state3), new Transition('\u2761', '\u2767', state3),
				new Transition('\u2190', '\u21F3', state3), new Transition('\u2132', '\u2132', state3),
				new Transition('\u2800', '\u28FF', state3), new Transition('\uFE64', '\uFE66', state3));
		state3.alter(true, 0);
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut120() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\uFF04', '\uFF04', state1), new Transition('\u00A2', '\u00A5', state1),
				new Transition('\u0024', '\u0024', state1), new Transition('\u17DB', '\u17DB', state1),
				new Transition('\uFFE0', '\uFFE1', state1), new Transition('\uFFE5', '\uFFE6', state1),
				new Transition('\uFE69', '\uFE69', state1), new Transition('\u0E3F', '\u0E3F', state1),
				new Transition('\u09F2', '\u09F3', state1), new Transition('\u20A0', '\u20AF', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut121() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0D80', '\u0DFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut122() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u0060', '\u0060', state1), new Transition('\u02B9', '\u02BA', state1),
				new Transition('\u02D2', '\u02DF', state1), new Transition('\u1FBF', '\u1FC1', state1),
				new Transition('\u00B4', '\u00B4', state1), new Transition('\uFF40', '\uFF40', state1),
				new Transition('\u0374', '\u0375', state1), new Transition('\u1FCD', '\u1FCF', state1),
				new Transition('\u1FED', '\u1FEF', state1), new Transition('\u00A8', '\u00A8', state1),
				new Transition('\u309B', '\u309C', state1), new Transition('\u00AF', '\u00AF', state1),
				new Transition('\uFFE3', '\uFFE3', state1), new Transition('\u02C2', '\u02CF', state1),
				new Transition('\u02E5', '\u02ED', state1), new Transition('\u1FBD', '\u1FBD', state1),
				new Transition('\uFF3E', '\uFF3E', state1), new Transition('\u1FFD', '\u1FFE', state1),
				new Transition('\u005E', '\u005E', state1), new Transition('\u0384', '\u0385', state1),
				new Transition('\u1FDD', '\u1FDF', state1), new Transition('\u00B8', '\u00B8', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut123() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\uDEC1', '\uDEC1', state2), new Transition('\uDEDB', '\uDEDB', state2),
				new Transition('\uDEFB', '\uDEFB', state2), new Transition('\uDF89', '\uDF89', state2),
				new Transition('\uDFA9', '\uDFA9', state2), new Transition('\uDFC3', '\uDFC3', state2),
				new Transition('\uDF15', '\uDF15', state2), new Transition('\uDF35', '\uDF35', state2),
				new Transition('\uDF4F', '\uDF4F', state2), new Transition('\uDF6F', '\uDF6F', state2));
		state1.alter(false, 1, new Transition('\u219A', '\u219B', state2), new Transition('\u25C1', '\u25C1', state2),
				new Transition('\uFF5C', '\uFF5C', state2), new Transition('\u21CE', '\u21CF', state2),
				new Transition('\uFB29', '\uFB29', state2), new Transition('\uD835', '\uD835', state0),
				new Transition('\uFF5E', '\uFF5E', state2), new Transition('\uFFE9', '\uFFEC', state2),
				new Transition('\u00F7', '\u00F7', state2), new Transition('\u2200', '\u22F1', state2),
				new Transition('\u25B7', '\u25B7', state2), new Transition('\u2044', '\u2044', state2),
				new Transition('\uFF1C', '\uFF1E', state2), new Transition('\u002B', '\u002B', state2),
				new Transition('\u21D2', '\u21D2', state2), new Transition('\u00AC', '\u00AC', state2),
				new Transition('\u2190', '\u2194', state2), new Transition('\u21A0', '\u21A0', state2),
				new Transition('\u2320', '\u2321', state2), new Transition('\u21D4', '\u21D4', state2),
				new Transition('\u21AE', '\u21AE', state2), new Transition('\u207A', '\u207C', state2),
				new Transition('\u266F', '\u266F', state2), new Transition('\u007C', '\u007C', state2),
				new Transition('\uFE62', '\uFE62', state2), new Transition('\uFFE2', '\uFFE2', state2),
				new Transition('\u21A3', '\u21A3', state2), new Transition('\u2308', '\u230B', state2),
				new Transition('\u003C', '\u003E', state2), new Transition('\u00D7', '\u00D7', state2),
				new Transition('\uFF0B', '\uFF0B', state2), new Transition('\u00B1', '\u00B1', state2),
				new Transition('\u007E', '\u007E', state2), new Transition('\u208A', '\u208C', state2),
				new Transition('\u21A6', '\u21A6', state2), new Transition('\uFE64', '\uFE66', state2));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut124() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFE50', '\uFE6F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut125() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDD2A', '\uDD64', state2), new Transition('\uDD00', '\uDD26', state2),
				new Transition('\uDD83', '\uDD84', state2), new Transition('\uDD8C', '\uDDA9', state2),
				new Transition('\uDDAE', '\uDDDD', state2), new Transition('\uDC00', '\uDCF5', state2),
				new Transition('\uDD6A', '\uDD6C', state2));
		state1.alter(false, 1, new Transition('\uD834', '\uD834', state0), new Transition('\uA490', '\uA4A1', state2),
				new Transition('\u2100', '\u2101', state2), new Transition('\u0F34', '\u0F34', state2),
				new Transition('\u3196', '\u319F', state2), new Transition('\u0F01', '\u0F03', state2),
				new Transition('\u0FCF', '\u0FCF', state2), new Transition('\u0F36', '\u0F36', state2),
				new Transition('\u2701', '\u2704', state2), new Transition('\u2E9B', '\u2EF3', state2),
				new Transition('\u3036', '\u3037', state2), new Transition('\u21D0', '\u21D1', state2),
				new Transition('\u3004', '\u3004', state2), new Transition('\u219C', '\u219F', state2),
				new Transition('\u2300', '\u2307', state2), new Transition('\u0F38', '\u0F38', state2),
				new Transition('\u2103', '\u2106', state2), new Transition('\u322A', '\u3243', state2),
				new Transition('\u21D3', '\u21D3', state2), new Transition('\u213A', '\u213A', state2),
				new Transition('\u328A', '\u32B0', state2), new Transition('\u2706', '\u2709', state2),
				new Transition('\u21A1', '\u21A2', state2), new Transition('\u2108', '\u2109', state2),
				new Transition('\u0B70', '\u0B70', state2), new Transition('\u3260', '\u327B', state2),
				new Transition('\u2670', '\u2671', state2), new Transition('\u2729', '\u274B', state2),
				new Transition('\u21A4', '\u21A5', state2), new Transition('\u2600', '\u2613', state2),
				new Transition('\u303E', '\u303F', state2), new Transition('\u2798', '\u27AF', state2),
				new Transition('\u00A6', '\u00A7', state2), new Transition('\u00A9', '\u00A9', state2),
				new Transition('\u3200', '\u321C', state2), new Transition('\u21A7', '\u21AD', state2),
				new Transition('\u3012', '\u3013', state2), new Transition('\u2440', '\u244A', state2),
				new Transition('\u25A0', '\u25B6', state2), new Transition('\uA4A4', '\uA4B3', state2),
				new Transition('\u3300', '\u3376', state2), new Transition('\u2114', '\u2114', state2),
				new Transition('\u00AE', '\u00AE', state2), new Transition('\u25C2', '\u25F7', state2),
				new Transition('\u0F13', '\u0F17', state2), new Transition('\u00B0', '\u00B0', state2),
				new Transition('\uFFE4', '\uFFE4', state2), new Transition('\u2400', '\u2426', state2),
				new Transition('\u2116', '\u2118', state2), new Transition('\u230C', '\u231F', state2),
				new Transition('\u327F', '\u327F', state2), new Transition('\u2619', '\u266E', state2),
				new Transition('\u2F00', '\u2FD5', state2), new Transition('\u274D', '\u274D', state2),
				new Transition('\u21D5', '\u21F3', state2), new Transition('\u0482', '\u0482', state2),
				new Transition('\u337B', '\u33DD', state2), new Transition('\uFFE8', '\uFFE8', state2),
				new Transition('\u06E9', '\u06E9', state2), new Transition('\u270C', '\u2727', state2),
				new Transition('\u00B6', '\u00B6', state2), new Transition('\u0F1A', '\u0F1F', state2),
				new Transition('\u274F', '\u2752', state2), new Transition('\u32D0', '\u32FE', state2),
				new Transition('\u27B1', '\u27BE', state2), new Transition('\u3020', '\u3020', state2),
				new Transition('\uFFED', '\uFFEE', state2), new Transition('\u211E', '\u2123', state2),
				new Transition('\uA4B5', '\uA4C0', state2), new Transition('\u2756', '\u2756', state2),
				new Transition('\u25B8', '\u25C0', state2), new Transition('\u2125', '\u2125', state2),
				new Transition('\u33E0', '\u33FE', state2), new Transition('\u2322', '\u2328', state2),
				new Transition('\u2500', '\u2595', state2), new Transition('\u2127', '\u2127', state2),
				new Transition('\u21AF', '\u21CD', state2), new Transition('\u232B', '\u237B', state2),
				new Transition('\u237D', '\u239A', state2), new Transition('\u2758', '\u275E', state2),
				new Transition('\u0FBE', '\u0FC5', state2), new Transition('\u2E80', '\u2E99', state2),
				new Transition('\u2129', '\u2129', state2), new Transition('\u2FF0', '\u2FFB', state2),
				new Transition('\u3190', '\u3191', state2), new Transition('\uA4C2', '\uA4C4', state2),
				new Transition('\uA4C6', '\uA4C6', state2), new Transition('\u32C0', '\u32CB', state2),
				new Transition('\u09FA', '\u09FA', state2), new Transition('\u2794', '\u2794', state2),
				new Transition('\u212E', '\u212E', state2), new Transition('\uFFFC', '\uFFFD', state2),
				new Transition('\u0FC7', '\u0FCC', state2), new Transition('\u249C', '\u24E9', state2),
				new Transition('\u06FD', '\u06FE', state2), new Transition('\u2195', '\u2199', state2),
				new Transition('\u2761', '\u2767', state2), new Transition('\u2132', '\u2132', state2),
				new Transition('\u2800', '\u28FF', state2));
		state2.alter(true, 2);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut126() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u02B0', '\u02FF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut127() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uFFF0', '\uFFFD', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut128() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u2070', '\u209F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut129() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0700', '\u074F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut130() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDC7F', state2));
		state1.alter(false, 0, new Transition('\uDB40', '\uDB40', state0));
		state2.alter(true, 0);
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut131() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0B80', '\u0BFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut132() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0C00', '\u0C7F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut133() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0780', '\u07BF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut134() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0E00', '\u0E7F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut135() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0F00', '\u0FFF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut136() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		State state28 = new State();
		State state29 = new State();
		State state30 = new State();
		State state31 = new State();
		State state32 = new State();
		State state33 = new State();
		State state34 = new State();
		State state35 = new State();
		State state36 = new State();
		State state37 = new State();
		State state38 = new State();
		State state39 = new State();
		State state40 = new State();
		State state41 = new State();
		State state42 = new State();
		State state43 = new State();
		State state44 = new State();
		State state45 = new State();
		State state46 = new State();
		State state47 = new State();
		State state48 = new State();
		State state49 = new State();
		State state50 = new State();
		State state51 = new State();
		State state52 = new State();
		State state53 = new State();
		State state54 = new State();
		State state55 = new State();
		State state56 = new State();
		State state57 = new State();
		State state58 = new State();
		state0.alter(false, 40, new Transition('\u0041', '\u0046', state42),
				new Transition('\u0061', '\u0066', state42), new Transition('\u0030', '\u0039', state42));
		state1.alter(false, 21, new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state12));
		state2.alter(false, 34, new Transition('\u0041', '\u0046', state48),
				new Transition('\u0061', '\u0066', state48), new Transition('\u0030', '\u0039', state48));
		state3.alter(false, 45, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17));
		state4.alter(false, 43, new Transition('\u003A', '\u003A', state0));
		state5.alter(false, 7, new Transition('\u002E', '\u002E', state31));
		state6.alter(false, 13, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state26),
				new Transition('\u0061', '\u0066', state26), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state7));
		state7.alter(false, 35, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state28),
				new Transition('\u0061', '\u0066', state28), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state28));
		state8.alter(false, 39, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17), new Transition('\u0041', '\u0046', state18),
				new Transition('\u0061', '\u0066', state18), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state11));
		state9.alter(false, 16, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17), new Transition('\u0041', '\u0046', state3),
				new Transition('\u0061', '\u0066', state3), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state3));
		state10.alter(false, 17, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state15),
				new Transition('\u0061', '\u0066', state15), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state6));
		state11.alter(false, 26, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17), new Transition('\u0041', '\u0046', state29),
				new Transition('\u0061', '\u0066', state29), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state9));
		state12.alter(false, 31, new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state5));
		state13.alter(false, 12, new Transition('\u002E', '\u002E', state38),
				new Transition('\u0030', '\u0039', state21));
		state14.alter(false, 25, new Transition('\u0041', '\u0046', state44),
				new Transition('\u0061', '\u0066', state44), new Transition('\u0030', '\u0039', state44));
		state15.alter(false, 15, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state26),
				new Transition('\u0061', '\u0066', state26), new Transition('\u0030', '\u0039', state26));
		state16.alter(false, 42, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state20), new Transition('\u0041', '\u0046', state42),
				new Transition('\u0061', '\u0066', state42), new Transition('\u0030', '\u0039', state42));
		state17.alter(false, 56, new Transition('\u0041', '\u0046', state42),
				new Transition('\u0061', '\u0066', state42), new Transition('\u0030', '\u0039', state8));
		state18.alter(false, 37, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17), new Transition('\u0041', '\u0046', state29),
				new Transition('\u0061', '\u0066', state29), new Transition('\u0030', '\u0039', state29));
		state19.alter(false, 14, new Transition('\u002E', '\u002E', state38));
		state20.alter(false, 11, new Transition('\u0030', '\u0039', state1));
		state21.alter(false, 46, new Transition('\u002E', '\u002E', state38),
				new Transition('\u0030', '\u0039', state19));
		state22.alter(false, 48, new Transition('\u002E', '\u002E', state43),
				new Transition('\u0030', '\u0039', state32));
		state23.alter(true, 2, new Transition('\u0061', '\u007A', state24), new Transition('\u003D', '\u003D', state24),
				new Transition('\u0024', '\u0024', state24), new Transition('\u0021', '\u0021', state24),
				new Transition('\u007E', '\u007E', state24), new Transition('\u005B', '\u005B', state41),
				new Transition('\u0025', '\u0025', state2), new Transition('\u005F', '\u005F', state24),
				new Transition('\u003F', '\u003F', state47), new Transition('\u0026', '\u003B', state24),
				new Transition('\u0040', '\u005A', state24), new Transition('\u0023', '\u0023', state58));
		state24.alter(true, 4, new Transition('\u0061', '\u007A', state24), new Transition('\u003D', '\u003D', state24),
				new Transition('\u0024', '\u0024', state24), new Transition('\u0021', '\u0021', state24),
				new Transition('\u007E', '\u007E', state24), new Transition('\u0025', '\u0025', state2),
				new Transition('\u005F', '\u005F', state24), new Transition('\u003F', '\u003F', state47),
				new Transition('\u0026', '\u003B', state24), new Transition('\u0040', '\u005A', state24),
				new Transition('\u0023', '\u0023', state58));
		state25.alter(true, 22, new Transition('\u0021', '\u0021', state24),
				new Transition('\u002F', '\u002F', state34), new Transition('\u0040', '\u005A', state24),
				new Transition('\u0023', '\u0023', state58), new Transition('\u0061', '\u007A', state24),
				new Transition('\u0030', '\u003B', state24), new Transition('\u003D', '\u003D', state24),
				new Transition('\u0024', '\u0024', state24), new Transition('\u0026', '\u002E', state24),
				new Transition('\u007E', '\u007E', state24), new Transition('\u0025', '\u0025', state2),
				new Transition('\u005F', '\u005F', state24), new Transition('\u003F', '\u003F', state47));
		state26.alter(false, 29, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state28),
				new Transition('\u0061', '\u0066', state28), new Transition('\u0030', '\u0039', state28));
		state27.alter(false, 1, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state15),
				new Transition('\u0061', '\u0066', state15), new Transition('\u0030', '\u0039', state15));
		state28.alter(false, 41, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state35));
		state29.alter(false, 5, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17), new Transition('\u0041', '\u0046', state3),
				new Transition('\u0061', '\u0066', state3), new Transition('\u0030', '\u0039', state3));
		state30.alter(false, 27, new Transition('\u002E', '\u002E', state43));
		state31.alter(false, 32, new Transition('\u0030', '\u0039', state13));
		state32.alter(false, 0, new Transition('\u002E', '\u002E', state43),
				new Transition('\u0030', '\u0039', state30));
		state33.alter(false, 47, new Transition('\u0041', '\u0046', state39),
				new Transition('\u0061', '\u0066', state39), new Transition('\u0030', '\u0039', state39));
		state34.alter(true, 30, new Transition('\u0040', '\u0040', state23),
				new Transition('\u0021', '\u0021', state44), new Transition('\u005B', '\u005B', state41),
				new Transition('\u002F', '\u002F', state24), new Transition('\u0023', '\u0023', state58),
				new Transition('\u0041', '\u005A', state44), new Transition('\u0061', '\u007A', state44),
				new Transition('\u0030', '\u003B', state44), new Transition('\u003D', '\u003D', state44),
				new Transition('\u0024', '\u0024', state44), new Transition('\u0026', '\u002E', state44),
				new Transition('\u007E', '\u007E', state44), new Transition('\u0025', '\u0025', state40),
				new Transition('\u005F', '\u005F', state44), new Transition('\u003F', '\u003F', state47));
		state35.alter(false, 49, new Transition('\u003A', '\u003A', state16),
				new Transition('\u0041', '\u0046', state27), new Transition('\u0061', '\u0066', state27),
				new Transition('\u0030', '\u0039', state10));
		state36.alter(false, 52, new Transition('\u005D', '\u005D', state51),
				new Transition('\u0030', '\u0039', state52));
		state37.alter(false, 18, new Transition('\u005D', '\u005D', state51));
		state38.alter(false, 36, new Transition('\u0030', '\u0039', state22));
		state39.alter(false, 44, new Transition('\u0041', '\u0046', state47),
				new Transition('\u0061', '\u0066', state47), new Transition('\u0030', '\u0039', state47));
		state40.alter(false, 51, new Transition('\u0041', '\u0046', state14),
				new Transition('\u0061', '\u0066', state14), new Transition('\u0030', '\u0039', state14));
		state41.alter(false, 33, new Transition('\u003A', '\u003A', state4),
				new Transition('\u0041', '\u0046', state27), new Transition('\u0061', '\u0066', state27),
				new Transition('\u0030', '\u0039', state27));
		state42.alter(false, 58, new Transition('\u005D', '\u005D', state51),
				new Transition('\u003A', '\u003A', state17), new Transition('\u0041', '\u0046', state18),
				new Transition('\u0061', '\u0066', state18), new Transition('\u0030', '\u0039', state18));
		state43.alter(false, 3, new Transition('\u0030', '\u0039', state36));
		state44.alter(true, 6, new Transition('\u0040', '\u0040', state23), new Transition('\u0021', '\u0021', state44),
				new Transition('\u002F', '\u002F', state24), new Transition('\u0023', '\u0023', state58),
				new Transition('\u0041', '\u005A', state44), new Transition('\u0061', '\u007A', state44),
				new Transition('\u0030', '\u003B', state44), new Transition('\u003D', '\u003D', state44),
				new Transition('\u0024', '\u0024', state44), new Transition('\u0026', '\u002E', state44),
				new Transition('\u007E', '\u007E', state44), new Transition('\u0025', '\u0025', state40),
				new Transition('\u005F', '\u005F', state44), new Transition('\u003F', '\u003F', state47));
		state45.alter(true, 38, new Transition('\u0021', '\u0021', state45),
				new Transition('\u003B', '\u003B', state45), new Transition('\u002F', '\u002F', state24),
				new Transition('\u0030', '\u0039', state45), new Transition('\u0040', '\u005A', state45),
				new Transition('\u0023', '\u0023', state58), new Transition('\u0061', '\u007A', state45),
				new Transition('\u003D', '\u003D', state45), new Transition('\u0024', '\u0024', state45),
				new Transition('\u0026', '\u002E', state45), new Transition('\u007E', '\u007E', state45),
				new Transition('\u0025', '\u0025', state49), new Transition('\u005F', '\u005F', state45),
				new Transition('\u003F', '\u003F', state47));
		state46.alter(false, 20, new Transition('\u0041', '\u0046', state45),
				new Transition('\u0061', '\u0066', state45), new Transition('\u0030', '\u0039', state45));
		state47.alter(true, 57, new Transition('\u0061', '\u007A', state47),
				new Transition('\u003D', '\u003D', state47), new Transition('\u005D', '\u005D', state47),
				new Transition('\u0024', '\u0024', state47), new Transition('\u0021', '\u0021', state47),
				new Transition('\u007E', '\u007E', state47), new Transition('\u0025', '\u0025', state33),
				new Transition('\u005F', '\u005F', state47), new Transition('\u0026', '\u003B', state47),
				new Transition('\u0023', '\u0023', state58), new Transition('\u003F', '\u005B', state47));
		state48.alter(false, 23, new Transition('\u0041', '\u0046', state24),
				new Transition('\u0061', '\u0066', state24), new Transition('\u0030', '\u0039', state24));
		state49.alter(false, 55, new Transition('\u0041', '\u0046', state46),
				new Transition('\u0061', '\u0066', state46), new Transition('\u0030', '\u0039', state46));
		state50.alter(true, 24, new Transition('\u0040', '\u0040', state45),
				new Transition('\u003A', '\u003A', state57), new Transition('\u002D', '\u002E', state50),
				new Transition('\u0021', '\u0021', state45), new Transition('\u003B', '\u003B', state45),
				new Transition('\u0026', '\u002A', state45), new Transition('\u002F', '\u002F', state24),
				new Transition('\u0030', '\u0039', state50), new Transition('\u0023', '\u0023', state58),
				new Transition('\u0041', '\u005A', state50), new Transition('\u0061', '\u007A', state50),
				new Transition('\u003D', '\u003D', state45), new Transition('\u0024', '\u0024', state45),
				new Transition('\u007E', '\u007E', state45), new Transition('\u002B', '\u002B', state50),
				new Transition('\u0025', '\u0025', state49), new Transition('\u005F', '\u005F', state45),
				new Transition('\u003F', '\u003F', state47), new Transition('\u002C', '\u002C', state45));
		state51.alter(true, 50, new Transition('\u003A', '\u003A', state55),
				new Transition('\u002F', '\u002F', state24), new Transition('\u003F', '\u003F', state47),
				new Transition('\u0023', '\u0023', state58));
		state52.alter(false, 8, new Transition('\u005D', '\u005D', state51),
				new Transition('\u0030', '\u0039', state37));
		state53.alter(false, 19, new Transition('\u0041', '\u0046', state58),
				new Transition('\u0061', '\u0066', state58), new Transition('\u0030', '\u0039', state58));
		state54.alter(true, 9, new Transition('\u0040', '\u0040', state45), new Transition('\u0021', '\u0021', state45),
				new Transition('\u003B', '\u003B', state45), new Transition('\u002F', '\u002F', state25),
				new Transition('\u0030', '\u0039', state45), new Transition('\u0023', '\u0023', state58),
				new Transition('\u0041', '\u005A', state50), new Transition('\u0061', '\u007A', state50),
				new Transition('\u003D', '\u003D', state45), new Transition('\u0024', '\u0024', state45),
				new Transition('\u0026', '\u002E', state45), new Transition('\u007E', '\u007E', state45),
				new Transition('\u0025', '\u0025', state49), new Transition('\u005F', '\u005F', state45));
		state55.alter(true, 54, new Transition('\u002F', '\u002F', state24),
				new Transition('\u0030', '\u0039', state55), new Transition('\u003F', '\u003F', state47),
				new Transition('\u0023', '\u0023', state58));
		state56.alter(false, 10, new Transition('\u0041', '\u0046', state53),
				new Transition('\u0061', '\u0066', state53), new Transition('\u0030', '\u0039', state53));
		state57.alter(false, 28, new Transition('\u0061', '\u007A', state47),
				new Transition('\u0030', '\u003B', state47), new Transition('\u003D', '\u003D', state47),
				new Transition('\u0024', '\u0024', state47), new Transition('\u0021', '\u0021', state47),
				new Transition('\u0026', '\u002E', state47), new Transition('\u007E', '\u007E', state47),
				new Transition('\u0025', '\u0025', state33), new Transition('\u002F', '\u002F', state25),
				new Transition('\u005F', '\u005F', state47), new Transition('\u003F', '\u005A', state47));
		state58.alter(true, 53, new Transition('\u0061', '\u007A', state58),
				new Transition('\u003D', '\u003D', state58), new Transition('\u005D', '\u005D', state58),
				new Transition('\u0024', '\u0024', state58), new Transition('\u0021', '\u0021', state58),
				new Transition('\u007E', '\u007E', state58), new Transition('\u0025', '\u0025', state56),
				new Transition('\u005F', '\u005F', state58), new Transition('\u0026', '\u003B', state58),
				new Transition('\u003F', '\u005B', state58));
		return Automaton.load(state54, true, null);
	}

	private static Automaton getAut137() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u1400', '\u167F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut138() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uA490', '\uA4CF', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut139() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\uA000', '\uA48F', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut140() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u0020', '\u0020', state1), new Transition('\u00A0', '\u00A0', state1),
				new Transition('\u1680', '\u1680', state1), new Transition('\u3000', '\u3000', state1),
				new Transition('\u2000', '\u200B', state1), new Transition('\u2028', '\u2029', state1),
				new Transition('\u202F', '\u202F', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut141() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\u2028', '\u2028', state1));
		state1.alter(true, 1);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut142() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u2029', '\u2029', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut143() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u0020', '\u0020', state1), new Transition('\u00A0', '\u00A0', state1),
				new Transition('\u1680', '\u1680', state1), new Transition('\u3000', '\u3000', state1),
				new Transition('\u2000', '\u200B', state1), new Transition('\u202F', '\u202F', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut144() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 0, new Transition('\u0030', '\u0039', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut145() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 0);
		state1.alter(false, 1, new Transition('\u0020', '\u0020', state0), new Transition('\u00A0', '\u00A0', state0),
				new Transition('\u1680', '\u1680', state0), new Transition('\u2000', '\u2000', state0),
				new Transition('\u3000', '\u3000', state0), new Transition('\u002D', '\u002D', state0),
				new Transition('\u200A', '\u200A', state0), new Transition('\u180E', '\u180E', state0),
				new Transition('\u202F', '\u202F', state0), new Transition('\u205F', '\u205F', state0),
				new Transition('\u0009', '\u0009', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut146() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 0, new Transition('\r', '\r', state2), new Transition('\u0085', '\u0085', state3),
				new Transition('\n', '\r', state3), new Transition('\u2028', '\u2029', state3));
		state1.alter(true, 0);
		state2.alter(false, 0, new Transition('\n', '\n', state1));
		state3.alter(true, 0);
		return Automaton.load(state0, false, null);
	}

	private static Automaton getAut147() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 1);
		state1.alter(false, 0, new Transition('\u0020', '\u0020', state0), new Transition('\u0009', '\r', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut148() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(true, 1);
		state1.alter(false, 0, new Transition('\u0085', '\u0085', state0), new Transition('\n', '\r', state0),
				new Transition('\u2028', '\u2029', state0));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut149() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		state0.alter(false, 0, new Transition('\uD801', '\uD801', state9), new Transition('\u3400', '\u3400', state6),
				new Transition('\u00C0', '\u00D6', state6), new Transition('\uAC00', '\uAC00', state6),
				new Transition('\uD800', '\uD800', state8), new Transition('\u0591', '\u05A1', state5),
				new Transition('\u0F35', '\u0F35', state5), new Transition('\u0C01', '\u0C03', state3),
				new Transition('\u1F20', '\u1F45', state6), new Transition('\u1320', '\u1346', state6),
				new Transition('\u0F37', '\u0F37', state5), new Transition('\u0B36', '\u0B39', state6),
				new Transition('\u3005', '\u3006', state6), new Transition('\u0F39', '\u0F39', state5),
				new Transition('\u0993', '\u09A8', state6), new Transition('\uFB38', '\uFB3C', state6),
				new Transition('\u0B3C', '\u0B3C', state5), new Transition('\u0CD5', '\u0CD6', state3),
				new Transition('\u0C05', '\u0C0C', state6), new Transition('\u30A1', '\u30FA', state6),
				new Transition('\u0670', '\u0670', state5), new Transition('\u0B3D', '\u0B3D', state6),
				new Transition('\uFB3E', '\uFB3E', state6), new Transition('\uFE70', '\uFE72', state6),
				new Transition('\u0A70', '\u0A71', state5), new Transition('\u20D0', '\u20DC', state5),
				new Transition('\u0B3E', '\u0B3E', state3), new Transition('\u0F3E', '\u0F3F', state3),
				new Transition('\u0B3F', '\u0B3F', state5), new Transition('\u0730', '\u074A', state5),
				new Transition('\u0A72', '\u0A74', state6), new Transition('\u166F', '\u1676', state6),
				new Transition('\uFE74', '\uFE74', state6), new Transition('\u0B40', '\u0B40', state3),
				new Transition('\u0D9A', '\u0DB1', state6), new Transition('\uFB40', '\uFB41', state6),
				new Transition('\u0B41', '\u0B43', state5), new Transition('\u0C0E', '\u0C10', state6),
				new Transition('\u00F8', '\u021F', state6), new Transition('\u1260', '\u1286', state6),
				new Transition('\u0F40', '\u0F47', state6), new Transition('\u0CDE', '\u0CDE', state6),
				new Transition('\uFB43', '\uFB44', state6), new Transition('\u0CE0', '\u0CE1', state6),
				new Transition('\u1000', '\u1021', state6), new Transition('\u09AA', '\u09B0', state6),
				new Transition('\u31A0', '\u31B7', state6), new Transition('\u20E1', '\u20E1', state5),
				new Transition('\u0B47', '\u0B48', state3), new Transition('\u05A3', '\u05B9', state5),
				new Transition('\u1F48', '\u1F4D', state6), new Transition('\u09B2', '\u09B2', state6),
				new Transition('\u0B4B', '\u0B4C', state3), new Transition('\uFD92', '\uFDC7', state6),
				new Transition('\u04D0', '\u04F5', state6), new Transition('\u0B4D', '\u0B4D', state5),
				new Transition('\u10D0', '\u10F6', state6), new Transition('\uF900', '\uFA2D', state6),
				new Transition('\u0E81', '\u0E82', state6), new Transition('\u4DB5', '\u4DB5', state6),
				new Transition('\u0A81', '\u0A82', state5), new Transition('\u0A83', '\u0A83', state3),
				new Transition('\u00D8', '\u00F6', state6), new Transition('\u0E84', '\u0E84', state6),
				new Transition('\uFF41', '\uFF5A', state6), new Transition('\u09B6', '\u09B9', state6),
				new Transition('\u0DB3', '\u0DBB', state6), new Transition('\u0C12', '\u0C28', state6),
				new Transition('\u1348', '\u135A', state6), new Transition('\u1F50', '\u1F57', state6),
				new Transition('\u0E87', '\u0E88', state6), new Transition('\u0250', '\u02AD', state6),
				new Transition('\u1288', '\u1288', state6), new Transition('\u0A85', '\u0A8B', state6),
				new Transition('\u09BC', '\u09BC', state5), new Transition('\u05BB', '\u05BD', state5),
				new Transition('\u0B56', '\u0B56', state5), new Transition('\u0DBD', '\u0DBD', state6),
				new Transition('\u0E8A', '\u0E8A', state6), new Transition('\u0B57', '\u0B57', state3),
				new Transition('\u1023', '\u1027', state6), new Transition('\u128A', '\u128D', state6),
				new Transition('\u05BF', '\u05BF', state5), new Transition('\u09BE', '\u09C0', state3),
				new Transition('\u1F59', '\u1F59', state6), new Transition('\u0A8D', '\u0A8D', state6),
				new Transition('\u0E8D', '\u0E8D', state6), new Transition('\u1F5B', '\u1F5B', state6),
				new Transition('\u05C1', '\u05C2', state5), new Transition('\u09C1', '\u09C4', state5),
				new Transition('\u0B5C', '\u0B5D', state6), new Transition('\u0F49', '\u0F6A', state6),
				new Transition('\u1029', '\u102A', state6), new Transition('\u1681', '\u169A', state6),
				new Transition('\u0A8F', '\u0A91', state6), new Transition('\u1F5D', '\u1F5D', state6),
				new Transition('\u0DC0', '\u0DC6', state6), new Transition('\u05C4', '\u05C4', state5),
				new Transition('\u04F8', '\u04F9', state6), new Transition('\u102C', '\u102C', state3),
				new Transition('\u0B5F', '\u0B61', state6), new Transition('\u302A', '\u302F', state5),
				new Transition('\u0360', '\u0362', state5), new Transition('\u09C7', '\u09C8', state3),
				new Transition('\u102D', '\u1030', state5), new Transition('\u0C2A', '\u0C33', state6),
				new Transition('\u0E94', '\u0E97', state6), new Transition('\u30FC', '\u30FE', state6),
				new Transition('\u0DCA', '\u0DCA', state5), new Transition('\u1031', '\u1031', state3),
				new Transition('\u1032', '\u1032', state5), new Transition('\u09CB', '\u09CC', state3),
				new Transition('\uD834', '\uD834', state4), new Transition('\uD834', '\uD834', state2),
				new Transition('\u3031', '\u3035', state6), new Transition('\u09CD', '\u09CD', state5),
				new Transition('\u0901', '\u0902', state5), new Transition('\u2102', '\u2102', state6),
				new Transition('\u0030', '\u0039', state1), new Transition('\uD835', '\uD835', state10),
				new Transition('\u0D02', '\u0D03', state3), new Transition('\u0E99', '\u0E9F', state6),
				new Transition('\u0903', '\u0903', state3), new Transition('\u1036', '\u1037', state5),
				new Transition('\u0DCF', '\u0DD1', state3), new Transition('\u0C35', '\u0C39', state6),
				new Transition('\u1038', '\u1038', state3), new Transition('\u1039', '\u1039', state5),
				new Transition('\u0A93', '\u0AA8', state6), new Transition('\u0DD2', '\u0DD4', state5),
				new Transition('\u2107', '\u2107', state6), new Transition('\u1290', '\u12AE', state6),
				new Transition('\u0EA1', '\u0EA3', state6), new Transition('\u0D05', '\u0D0C', state6),
				new Transition('\u0DD6', '\u0DD6', state5), new Transition('\u09D7', '\u09D7', state3),
				new Transition('\u1F5F', '\u1F7D', state6), new Transition('\u0EA5', '\u0EA5', state6),
				new Transition('\u11A8', '\u11F9', state6), new Transition('\u0C3E', '\u0C40', state5),
				new Transition('\u0EA7', '\u0EA7', state6), new Transition('\uD840', '\uD840', state11),
				new Transition('\u0D0E', '\u0D10', state6), new Transition('\u210A', '\u2113', state6),
				new Transition('\u0DD8', '\u0DDF', state3), new Transition('\u0C41', '\u0C44', state3),
				new Transition('\u09DC', '\u09DD', state6), new Transition('\u0EAA', '\u0EAB', state6),
				new Transition('\u0671', '\u06D3', state6), new Transition('\u0F71', '\u0F7E', state5),
				new Transition('\u05D0', '\u05EA', state6), new Transition('\u09DF', '\u09E1', state6),
				new Transition('\u037A', '\u037A', state6), new Transition('\u0AAA', '\u0AB0', state6),
				new Transition('\u0C46', '\u0C48', state5), new Transition('\u2115', '\u2115', state6),
				new Transition('\u0EAD', '\u0EB0', state6), new Transition('\u09E2', '\u09E3', state5),
				new Transition('\u12B0', '\u12B0', state6), new Transition('\u0EB1', '\u0EB1', state5),
				new Transition('\u0C4A', '\u0C4D', state5), new Transition('\u0F7F', '\u0F7F', state3),
				new Transition('\u0AB2', '\u0AB3', state6), new Transition('\u0EB2', '\u0EB3', state6),
				new Transition('\u0400', '\u0481', state6), new Transition('\u12B2', '\u12B5', state6),
				new Transition('\u02B0', '\u02B8', state6), new Transition('\u2119', '\u211D', state6),
				new Transition('\u0B82', '\u0B82', state5), new Transition('\u0F80', '\u0F84', state5),
				new Transition('\u3105', '\u312C', state6), new Transition('\u0B83', '\u0B83', state3),
				new Transition('\u0041', '\u005A', state6), new Transition('\u0EB4', '\u0EB9', state5),
				new Transition('\u0AB5', '\u0AB9', state6), new Transition('\uFB46', '\uFBB1', state6),
				new Transition('\u0D12', '\u0D28', state6), new Transition('\u0386', '\u0386', state6),
				new Transition('\u1050', '\u1055', state6), new Transition('\u0F86', '\u0F87', state5),
				new Transition('\u1820', '\u1877', state6), new Transition('\uA000', '\uA48C', state6),
				new Transition('\u0B85', '\u0B8A', state6), new Transition('\u12B8', '\u12BE', state6),
				new Transition('\u0EBB', '\u0EBC', state5), new Transition('\u0ABC', '\u0ABC', state5),
				new Transition('\u0C55', '\u0C56', state5), new Transition('\u0388', '\u038A', state6),
				new Transition('\u0ABD', '\u0ABD', state6), new Transition('\u0EBD', '\u0EBD', state6),
				new Transition('\u0F88', '\u0F8B', state6), new Transition('\u1056', '\u1057', state3),
				new Transition('\u09F0', '\u09F1', state6), new Transition('\u2124', '\u2124', state6),
				new Transition('\u0905', '\u0939', state6), new Transition('\u05F0', '\u05F2', state6),
				new Transition('\u02BB', '\u02C1', state6), new Transition('\u1058', '\u1059', state5),
				new Transition('\u038C', '\u038C', state6), new Transition('\u0ABE', '\u0AC0', state3),
				new Transition('\u0DF2', '\u0DF3', state3), new Transition('\u2126', '\u2126', state6),
				new Transition('\u12C0', '\u12C0', state6), new Transition('\u2128', '\u2128', state6),
				new Transition('\u0B8E', '\u0B90', state6), new Transition('\u0EC0', '\u0EC4', state6),
				new Transition('\u0AC1', '\u0AC5', state5), new Transition('\u12C2', '\u12C5', state6),
				new Transition('\uFDF0', '\uFDFB', state6), new Transition('\u212A', '\u212D', state6),
				new Transition('\u005F', '\u005F', state1), new Transition('\u0EC6', '\u0EC6', state6),
				new Transition('\u0B92', '\u0B95', state6), new Transition('\u0C60', '\u0C61', state6),
				new Transition('\uFE76', '\uFEFC', state6), new Transition('\u0F90', '\u0F97', state5),
				new Transition('\u0AC7', '\u0AC8', state5), new Transition('\u0AC9', '\u0AC9', state3),
				new Transition('\u0780', '\u07A5', state6), new Transition('\u212F', '\u2131', state6),
				new Transition('\u0EC8', '\u0ECD', state5), new Transition('\u12C8', '\u12CE', state6),
				new Transition('\u0ACB', '\u0ACC', state3), new Transition('\u16A0', '\u16EA', state6),
				new Transition('\u038E', '\u03A1', state6), new Transition('\u0D2A', '\u0D39', state6),
				new Transition('\u0B99', '\u0B9A', state6), new Transition('\u4E00', '\u4E00', state6),
				new Transition('\u0ACD', '\u0ACD', state5), new Transition('\uFF66', '\uFFBE', state6),
				new Transition('\uD869', '\uD869', state7), new Transition('\u0A02', '\u0A02', state5),
				new Transition('\u1100', '\u1159', state6), new Transition('\u0B9C', '\u0B9C', state6),
				new Transition('\u0AD0', '\u0AD0', state6), new Transition('\u2133', '\u2139', state6),
				new Transition('\u1200', '\u1206', state6), new Transition('\u02D0', '\u02D1', state6),
				new Transition('\u0B9E', '\u0B9F', state6), new Transition('\u1780', '\u17B3', state6),
				new Transition('\u1F80', '\u1FB4', state6), new Transition('\u12D0', '\u12D6', state6),
				new Transition('\u0A05', '\u0A0A', state6), new Transition('\u06D5', '\u06D5', state6),
				new Transition('\u1EA0', '\u1EF9', state6), new Transition('\uD7A3', '\uD7A3', state6),
				new Transition('\u093C', '\u093C', state5), new Transition('\u0061', '\u007A', state6),
				new Transition('\u093D', '\u093D', state6), new Transition('\u0BA3', '\u0BA4', state6),
				new Transition('\u9FA5', '\u9FA5', state6), new Transition('\u093E', '\u0940', state3),
				new Transition('\u0D3E', '\u0D40', state3), new Transition('\u3041', '\u3094', state6),
				new Transition('\u06D6', '\u06DC', state5), new Transition('\u0D41', '\u0D43', state5),
				new Transition('\u0A0F', '\u0A10', state6), new Transition('\u0BA8', '\u0BAA', state6),
				new Transition('\u0EDC', '\u0EDD', state6), new Transition('\u0941', '\u0948', state5),
				new Transition('\u07A6', '\u07B0', state5), new Transition('\u0AE0', '\u0AE0', state6),
				new Transition('\u0531', '\u0556', state6), new Transition('\u0D46', '\u0D48', state3),
				new Transition('\u0F99', '\u0FBC', state5), new Transition('\u06DF', '\u06E4', state5),
				new Transition('\u02E0', '\u02E4', state6), new Transition('\uD87E', '\uD87E', state12),
				new Transition('\u0949', '\u094C', state3), new Transition('\u0D4A', '\u0D4C', state3),
				new Transition('\u12D8', '\u12EE', state6), new Transition('\u0BAE', '\u0BB5', state6),
				new Transition('\u207F', '\u207F', state6), new Transition('\u06E5', '\u06E6', state6),
				new Transition('\u094D', '\u094D', state5), new Transition('\u0D4D', '\u0D4D', state5),
				new Transition('\u06E7', '\u06E8', state5), new Transition('\u17B4', '\u17B6', state3),
				new Transition('\u0C82', '\u0C83', state3), new Transition('\u0950', '\u0950', state6),
				new Transition('\u0E01', '\u0E30', state6), new Transition('\u0483', '\u0486', state5),
				new Transition('\u0BB7', '\u0BB9', state6), new Transition('\u06EA', '\u06ED', state5),
				new Transition('\u0A13', '\u0A28', state6), new Transition('\u0951', '\u0954', state5),
				new Transition('\u1FB6', '\u1FBC', state6), new Transition('\u17B7', '\u17BD', state5),
				new Transition('\u02EE', '\u02EE', state6), new Transition('\uFE20', '\uFE23', state5),
				new Transition('\u0C85', '\u0C8C', state6), new Transition('\u03A3', '\u03CE', state6),
				new Transition('\u0D57', '\u0D57', state3), new Transition('\u1FBE', '\u1FBE', state6),
				new Transition('\u0BBE', '\u0BBF', state3), new Transition('\u0559', '\u0559', state6),
				new Transition('\u0BC0', '\u0BC0', state5), new Transition('\u0BC1', '\u0BC2', state3),
				new Transition('\u17BE', '\u17C5', state3), new Transition('\u0C8E', '\u0C90', state6),
				new Transition('\u1FC2', '\u1FC4', state6), new Transition('\u0958', '\u0961', state6),
				new Transition('\u0222', '\u0233', state6), new Transition('\uFFC2', '\uFFC7', state6),
				new Transition('\u0FC6', '\u0FC6', state5), new Transition('\u17C6', '\u17C6', state5),
				new Transition('\u1208', '\u1246', state6), new Transition('\u0D60', '\u0D61', state6),
				new Transition('\u0A2A', '\u0A30', state6), new Transition('\u0BC6', '\u0BC8', state3),
				new Transition('\u17C7', '\u17C8', state3), new Transition('\u06FA', '\u06FC', state6),
				new Transition('\u0962', '\u0963', state5), new Transition('\u0621', '\u063A', state6),
				new Transition('\u1FC6', '\u1FCC', state6), new Transition('\u0E31', '\u0E31', state5),
				new Transition('\u1880', '\u18A8', state6), new Transition('\u0BCA', '\u0BCC', state3),
				new Transition('\u0A32', '\u0A33', state6), new Transition('\u0E32', '\u0E33', state6),
				new Transition('\u0F00', '\u0F00', state6), new Transition('\u3099', '\u309A', state5),
				new Transition('\u0BCD', '\u0BCD', state5), new Transition('\uFFCA', '\uFFCF', state6),
				new Transition('\u0B01', '\u0B01', state5), new Transition('\u12F0', '\u130E', state6),
				new Transition('\u17C9', '\u17D3', state5), new Transition('\u0A35', '\u0A36', state6),
				new Transition('\u3131', '\u318E', state6), new Transition('\u0B02', '\u0B03', state3),
				new Transition('\u309D', '\u309E', state6), new Transition('\uFB00', '\uFB06', state6),
				new Transition('\u0E34', '\u0E3A', state5), new Transition('\u1FD0', '\u1FD3', state6),
				new Transition('\u0A38', '\u0A39', state6), new Transition('\u0C92', '\u0CA8', state6),
				new Transition('\u13A0', '\u13F4', state6), new Transition('\u03D0', '\u03D7', state6),
				new Transition('\u0A3C', '\u0A3C', state5), new Transition('\uFFD2', '\uFFD7', state6),
				new Transition('\u0B05', '\u0B0C', state6), new Transition('\u0BD7', '\u0BD7', state3),
				new Transition('\u0A3E', '\u0A40', state3), new Transition('\u1FD6', '\u1FDB', state6),
				new Transition('\u1F00', '\u1F15', state6), new Transition('\u1401', '\u166C', state6),
				new Transition('\u0A41', '\u0A42', state5), new Transition('\uFD50', '\uFD8F', state6),
				new Transition('\uFFDA', '\uFFDC', state6), new Transition('\u18A9', '\u18A9', state5),
				new Transition('\u0B0F', '\u0B10', state6), new Transition('\u0710', '\u0710', state6),
				new Transition('\u1310', '\u1310', state6), new Transition('\u00AA', '\u00AA', state6),
				new Transition('\u0E40', '\u0E46', state6), new Transition('\u0711', '\u0711', state5),
				new Transition('\u0561', '\u0587', state6), new Transition('\uFBD3', '\uFD3D', state6),
				new Transition('\u0640', '\u064A', state6), new Transition('\u1312', '\u1315', state6),
				new Transition('\u048C', '\u04C4', state6), new Transition('\u0A47', '\u0A48', state5),
				new Transition('\u1248', '\u1248', state6), new Transition('\u0CAA', '\u0CB3', state6),
				new Transition('\uFB13', '\uFB17', state6), new Transition('\u0E47', '\u0E4E', state5),
				new Transition('\u124A', '\u124D', state6), new Transition('\u0F18', '\u0F19', state5),
				new Transition('\u0A4B', '\u0A4D', state5), new Transition('\u1FE0', '\u1FEC', state6),
				new Transition('\u0981', '\u0981', state5), new Transition('\u1F18', '\u1F1D', state6),
				new Transition('\u00B5', '\u00B5', state6), new Transition('\u1318', '\u131E', state6),
				new Transition('\u0982', '\u0983', state3), new Transition('\u0D82', '\u0D83', state3),
				new Transition('\u10A0', '\u10C5', state6), new Transition('\uFB1E', '\uFB1E', state5),
				new Transition('\u03DA', '\u03F5', state6), new Transition('\u0CB5', '\u0CB9', state6),
				new Transition('\uFB1D', '\uFB1D', state6), new Transition('\u064B', '\u0655', state5),
				new Transition('\u0B13', '\u0B28', state6), new Transition('\u00BA', '\u00BA', state6),
				new Transition('\u1250', '\u1256', state6), new Transition('\u115F', '\u11A2', state6),
				new Transition('\u0712', '\u072C', state6), new Transition('\u0985', '\u098C', state6),
				new Transition('\uFB1F', '\uFB28', state6), new Transition('\u0CBE', '\u0CBE', state3),
				new Transition('\u1258', '\u1258', state6), new Transition('\u0CBF', '\u0CBF', state5),
				new Transition('\u1FF2', '\u1FF4', state6), new Transition('\u0A59', '\u0A5C', state6),
				new Transition('\u125A', '\u125D', state6), new Transition('\u0D85', '\u0D96', state6),
				new Transition('\u0CC0', '\u0CC4', state3), new Transition('\u098F', '\u0990', state6),
				new Transition('\u1E00', '\u1E9B', state6), new Transition('\u0A5E', '\u0A5E', state6),
				new Transition('\u0CC6', '\u0CC6', state5), new Transition('\u1FF6', '\u1FFC', state6),
				new Transition('\u0B2A', '\u0B30', state6), new Transition('\u04C7', '\u04C8', state6),
				new Transition('\u0CC7', '\u0CC8', state3), new Transition('\u0300', '\u034E', state5),
				new Transition('\uFB2A', '\uFB36', state6), new Transition('\uFF21', '\uFF3A', state6),
				new Transition('\u0CCA', '\u0CCB', state3), new Transition('\u04CB', '\u04CC', state6),
				new Transition('\u0B32', '\u0B33', state6), new Transition('\u0CCC', '\u0CCD', state5));
		state1.alter(true, 1);
		state2.alter(false, 0, new Transition('\uDD6D', '\uDD72', state3), new Transition('\uDD65', '\uDD66', state3));
		state3.alter(true, 0);
		state4.alter(false, 0, new Transition('\uDD7B', '\uDD82', state5), new Transition('\uDD67', '\uDD69', state5),
				new Transition('\uDD85', '\uDD8B', state5), new Transition('\uDDAA', '\uDDAD', state5));
		state5.alter(true, 0);
		state6.alter(true, 0);
		state7.alter(false, 0, new Transition('\uDED6', '\uDED6', state6));
		state8.alter(false, 0, new Transition('\uDF00', '\uDF1E', state6), new Transition('\uDF30', '\uDF49', state6));
		state9.alter(false, 0, new Transition('\uDC28', '\uDC4D', state6), new Transition('\uDC00', '\uDC25', state6));
		state10.alter(false, 0, new Transition('\uDD4A', '\uDD50', state6), new Transition('\uDCAE', '\uDCB9', state6),
				new Transition('\uDC56', '\uDC9C', state6), new Transition('\uDD16', '\uDD1C', state6),
				new Transition('\uDD40', '\uDD44', state6), new Transition('\uDF8A', '\uDFA8', state6),
				new Transition('\uDCC2', '\uDCC3', state6), new Transition('\uDD52', '\uDEA3', state6),
				new Transition('\uDF16', '\uDF34', state6), new Transition('\uDCA9', '\uDCAC', state6),
				new Transition('\uDD0D', '\uDD14', state6), new Transition('\uDF36', '\uDF4E', state6),
				new Transition('\uDEA8', '\uDEC0', state6), new Transition('\uDEC2', '\uDEDA', state6),
				new Transition('\uDD46', '\uDD46', state6), new Transition('\uDC9E', '\uDC9F', state6),
				new Transition('\uDCC5', '\uDD05', state6), new Transition('\uDFAA', '\uDFC2', state6),
				new Transition('\uDEDC', '\uDEFA', state6), new Transition('\uDCBB', '\uDCBB', state6),
				new Transition('\uDD1E', '\uDD39', state6), new Transition('\uDFC4', '\uDFC9', state6),
				new Transition('\uDD07', '\uDD0A', state6), new Transition('\uDCA2', '\uDCA2', state6),
				new Transition('\uDF50', '\uDF6E', state6), new Transition('\uDEFC', '\uDF14', state6),
				new Transition('\uDD3B', '\uDD3E', state6), new Transition('\uDC00', '\uDC54', state6),
				new Transition('\uDCA5', '\uDCA6', state6), new Transition('\uDF70', '\uDF88', state6),
				new Transition('\uDCBD', '\uDCC0', state6));
		state11.alter(false, 0, new Transition('\uDC00', '\uDC00', state6));
		state12.alter(false, 0, new Transition('\uDC00', '\uDE1D', state6));
		return Automaton.load(state0, false, null);
	}

	private static Automaton getAut150() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		State state28 = new State();
		State state29 = new State();
		State state30 = new State();
		State state31 = new State();
		State state32 = new State();
		State state33 = new State();
		State state34 = new State();
		State state35 = new State();
		State state36 = new State();
		State state37 = new State();
		State state38 = new State();
		State state39 = new State();
		State state40 = new State();
		State state41 = new State();
		State state42 = new State();
		State state43 = new State();
		State state44 = new State();
		State state45 = new State();
		State state46 = new State();
		State state47 = new State();
		State state48 = new State();
		State state49 = new State();
		State state50 = new State();
		State state51 = new State();
		State state52 = new State();
		State state53 = new State();
		State state54 = new State();
		State state55 = new State();
		State state56 = new State();
		State state57 = new State();
		State state58 = new State();
		State state59 = new State();
		State state60 = new State();
		State state61 = new State();
		state0.alter(false, 54, new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state10));
		state1.alter(false, 5, new Transition('\u0041', '\u0046', state42), new Transition('\u0061', '\u0066', state42),
				new Transition('\u0030', '\u0039', state42));
		state2.alter(false, 35, new Transition('\u0041', '\u0046', state49),
				new Transition('\u0061', '\u0066', state49), new Transition('\u0030', '\u0039', state49));
		state3.alter(false, 19, new Transition('\u003A', '\u003A', state1));
		state4.alter(false, 51, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16));
		state5.alter(false, 17, new Transition('\u002E', '\u002E', state31));
		state6.alter(false, 40, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16), new Transition('\u0041', '\u0046', state4),
				new Transition('\u0061', '\u0066', state4), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state4));
		state7.alter(false, 53, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state28),
				new Transition('\u0061', '\u0066', state28), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state11));
		state8.alter(false, 11, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16), new Transition('\u0041', '\u0046', state29),
				new Transition('\u0061', '\u0066', state29), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state6));
		state9.alter(false, 56, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state17),
				new Transition('\u0061', '\u0066', state17), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state7));
		state10.alter(false, 52, new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state5));
		state11.alter(false, 38, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state26),
				new Transition('\u0061', '\u0066', state26), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state26));
		state12.alter(false, 57, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16), new Transition('\u0041', '\u0046', state18),
				new Transition('\u0061', '\u0066', state18), new Transition('\u002E', '\u002E', state31),
				new Transition('\u0030', '\u0039', state8));
		state13.alter(false, 4, new Transition('\u002E', '\u002E', state38),
				new Transition('\u0030', '\u0039', state21));
		state14.alter(false, 29, new Transition('\u0041', '\u0046', state44),
				new Transition('\u0061', '\u0066', state44), new Transition('\u0030', '\u0039', state44));
		state15.alter(false, 33, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state20), new Transition('\u0041', '\u0046', state42),
				new Transition('\u0061', '\u0066', state42), new Transition('\u0030', '\u0039', state42));
		state16.alter(false, 22, new Transition('\u0041', '\u0046', state42),
				new Transition('\u0061', '\u0066', state42), new Transition('\u0030', '\u0039', state12));
		state17.alter(false, 28, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state28),
				new Transition('\u0061', '\u0066', state28), new Transition('\u0030', '\u0039', state28));
		state18.alter(false, 31, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16), new Transition('\u0041', '\u0046', state29),
				new Transition('\u0061', '\u0066', state29), new Transition('\u0030', '\u0039', state29));
		state19.alter(false, 23, new Transition('\u002E', '\u002E', state38));
		state20.alter(false, 37, new Transition('\u0030', '\u0039', state0));
		state21.alter(false, 55, new Transition('\u002E', '\u002E', state38),
				new Transition('\u0030', '\u0039', state19));
		state22.alter(false, 20, new Transition('\u002E', '\u002E', state43),
				new Transition('\u0030', '\u0039', state32));
		state23.alter(false, 60, new Transition('\u0061', '\u007A', state23),
				new Transition('\u007D', '\u007D', state59), new Transition('\u003D', '\u003D', state23),
				new Transition('\u0024', '\u0024', state23), new Transition('\u0021', '\u0021', state23),
				new Transition('\u007E', '\u007E', state23), new Transition('\u0025', '\u0025', state2),
				new Transition('\u003F', '\u003F', state48), new Transition('\u005F', '\u005F', state23),
				new Transition('\u0026', '\u003B', state23), new Transition('\u0040', '\u005A', state23),
				new Transition('\u0023', '\u0023', state60));
		state24.alter(false, 6, new Transition('\u0021', '\u0021', state23),
				new Transition('\u005B', '\u005B', state41), new Transition('\u0040', '\u005A', state23),
				new Transition('\u0023', '\u0023', state60), new Transition('\u0061', '\u007A', state23),
				new Transition('\u007D', '\u007D', state59), new Transition('\u003D', '\u003D', state23),
				new Transition('\u0024', '\u0024', state23), new Transition('\u007E', '\u007E', state23),
				new Transition('\u0025', '\u0025', state2), new Transition('\u003F', '\u003F', state48),
				new Transition('\u005F', '\u005F', state23), new Transition('\u0026', '\u003B', state23));
		state25.alter(false, 21, new Transition('\u0021', '\u0021', state23),
				new Transition('\u002F', '\u002F', state34), new Transition('\u0040', '\u005A', state23),
				new Transition('\u0023', '\u0023', state60), new Transition('\u0061', '\u007A', state23),
				new Transition('\u007D', '\u007D', state59), new Transition('\u0030', '\u003B', state23),
				new Transition('\u003D', '\u003D', state23), new Transition('\u0024', '\u0024', state23),
				new Transition('\u0026', '\u002E', state23), new Transition('\u007E', '\u007E', state23),
				new Transition('\u0025', '\u0025', state2), new Transition('\u003F', '\u003F', state48),
				new Transition('\u005F', '\u005F', state23));
		state26.alter(false, 46, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35));
		state27.alter(false, 48, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state17),
				new Transition('\u0061', '\u0066', state17), new Transition('\u0030', '\u0039', state17));
		state28.alter(false, 2, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state35), new Transition('\u0041', '\u0046', state26),
				new Transition('\u0061', '\u0066', state26), new Transition('\u0030', '\u0039', state26));
		state29.alter(false, 25, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16), new Transition('\u0041', '\u0046', state4),
				new Transition('\u0061', '\u0066', state4), new Transition('\u0030', '\u0039', state4));
		state30.alter(false, 61, new Transition('\u002E', '\u002E', state43));
		state31.alter(false, 16, new Transition('\u0030', '\u0039', state13));
		state32.alter(false, 30, new Transition('\u002E', '\u002E', state43),
				new Transition('\u0030', '\u0039', state30));
		state33.alter(false, 15, new Transition('\u0041', '\u0046', state39),
				new Transition('\u0061', '\u0066', state39), new Transition('\u0030', '\u0039', state39));
		state34.alter(false, 14, new Transition('\u0040', '\u0040', state24),
				new Transition('\u0021', '\u0021', state44), new Transition('\u005B', '\u005B', state41),
				new Transition('\u002F', '\u002F', state23), new Transition('\u0023', '\u0023', state60),
				new Transition('\u0041', '\u005A', state44), new Transition('\u0061', '\u007A', state44),
				new Transition('\u007D', '\u007D', state59), new Transition('\u0030', '\u003B', state44),
				new Transition('\u003D', '\u003D', state44), new Transition('\u0024', '\u0024', state44),
				new Transition('\u0026', '\u002E', state44), new Transition('\u007E', '\u007E', state44),
				new Transition('\u0025', '\u0025', state40), new Transition('\u003F', '\u003F', state48),
				new Transition('\u005F', '\u005F', state44));
		state35.alter(false, 27, new Transition('\u003A', '\u003A', state15),
				new Transition('\u0041', '\u0046', state27), new Transition('\u0061', '\u0066', state27),
				new Transition('\u0030', '\u0039', state9));
		state36.alter(false, 47, new Transition('\u005D', '\u005D', state52),
				new Transition('\u0030', '\u0039', state53));
		state37.alter(false, 18, new Transition('\u005D', '\u005D', state52));
		state38.alter(false, 8, new Transition('\u0030', '\u0039', state22));
		state39.alter(false, 13, new Transition('\u0041', '\u0046', state48),
				new Transition('\u0061', '\u0066', state48), new Transition('\u0030', '\u0039', state48));
		state40.alter(false, 1, new Transition('\u0041', '\u0046', state14),
				new Transition('\u0061', '\u0066', state14), new Transition('\u0030', '\u0039', state14));
		state41.alter(false, 59, new Transition('\u003A', '\u003A', state3),
				new Transition('\u0041', '\u0046', state27), new Transition('\u0061', '\u0066', state27),
				new Transition('\u0030', '\u0039', state27));
		state42.alter(false, 3, new Transition('\u005D', '\u005D', state52),
				new Transition('\u003A', '\u003A', state16), new Transition('\u0041', '\u0046', state18),
				new Transition('\u0061', '\u0066', state18), new Transition('\u0030', '\u0039', state18));
		state43.alter(false, 49, new Transition('\u0030', '\u0039', state36));
		state44.alter(false, 58, new Transition('\u0040', '\u0040', state24),
				new Transition('\u0021', '\u0021', state44), new Transition('\u002F', '\u002F', state23),
				new Transition('\u0023', '\u0023', state60), new Transition('\u0041', '\u005A', state44),
				new Transition('\u0061', '\u007A', state44), new Transition('\u007D', '\u007D', state59),
				new Transition('\u0030', '\u003B', state44), new Transition('\u003D', '\u003D', state44),
				new Transition('\u0024', '\u0024', state44), new Transition('\u0026', '\u002E', state44),
				new Transition('\u007E', '\u007E', state44), new Transition('\u0025', '\u0025', state40),
				new Transition('\u003F', '\u003F', state48), new Transition('\u005F', '\u005F', state44));
		state45.alter(false, 41, new Transition('\u0B99', '\u0B9A', state61),
				new Transition('\u00C0', '\u00D6', state61), new Transition('\u0B9C', '\u0B9C', state61),
				new Transition('\u1F20', '\u1F45', state61), new Transition('\u119E', '\u119E', state61),
				new Transition('\u0B36', '\u0B39', state61), new Transition('\u06D0', '\u06D3', state61),
				new Transition('\u0B9E', '\u0B9F', state61), new Transition('\u1F80', '\u1FB4', state61),
				new Transition('\u0993', '\u09A8', state61), new Transition('\u0134', '\u013E', state61),
				new Transition('\u3007', '\u3007', state61), new Transition('\u0401', '\u040C', state61),
				new Transition('\u0A05', '\u0A0A', state61), new Transition('\u06D5', '\u06D5', state61),
				new Transition('\u1EA0', '\u1EF9', state61), new Transition('\u113C', '\u113C', state61),
				new Transition('\u0C05', '\u0C0C', state61), new Transition('\u0061', '\u007A', state61),
				new Transition('\u30A1', '\u30FA', state61), new Transition('\u093D', '\u093D', state61),
				new Transition('\u0B3D', '\u0B3D', state61), new Transition('\u0BA3', '\u0BA4', state61),
				new Transition('\u113E', '\u113E', state61), new Transition('\u01FA', '\u0217', state61),
				new Transition('\u3041', '\u3094', state61), new Transition('\u045E', '\u0481', state61),
				new Transition('\u0A72', '\u0A74', state61), new Transition('\u1140', '\u1140', state61),
				new Transition('\u11A8', '\u11A8', state61), new Transition('\u0180', '\u01C3', state61),
				new Transition('\u0C0E', '\u0C10', state61), new Transition('\u0A0F', '\u0A10', state61),
				new Transition('\u0BA8', '\u0BAA', state61), new Transition('\u0F40', '\u0F47', state61),
				new Transition('\u0CDE', '\u0CDE', state61), new Transition('\u11AB', '\u11AB', state61),
				new Transition('\u0141', '\u0148', state61), new Transition('\u0AE0', '\u0AE0', state61),
				new Transition('\u04D0', '\u04EB', state61), new Transition('\u0CE0', '\u0CE1', state61),
				new Transition('\u0531', '\u0556', state61), new Transition('\u09AA', '\u09B0', state61),
				new Transition('\u007B', '\u007B', state55), new Transition('\u11AE', '\u11AF', state61),
				new Transition('\u1F48', '\u1F4D', state61), new Transition('\u09B2', '\u09B2', state61),
				new Transition('\u0BAE', '\u0BB5', state61), new Transition('\u06E5', '\u06E6', state61),
				new Transition('\u114C', '\u114C', state61), new Transition('\u10D0', '\u10F6', state61),
				new Transition('\u114E', '\u114E', state61), new Transition('\u0E81', '\u0E82', state61),
				new Transition('\u0E01', '\u0E2E', state61), new Transition('\u1150', '\u1150', state61),
				new Transition('\u00D8', '\u00F6', state61), new Transition('\u0E84', '\u0E84', state61),
				new Transition('\u11B7', '\u11B8', state61), new Transition('\u09B6', '\u09B9', state61),
				new Transition('\u0250', '\u02A8', state61), new Transition('\u0BB7', '\u0BB9', state61),
				new Transition('\u0C12', '\u0C28', state61), new Transition('\u0A13', '\u0A28', state61),
				new Transition('\u1FB6', '\u1FBC', state61), new Transition('\u11BA', '\u11BA', state61),
				new Transition('\u1F50', '\u1F57', state61), new Transition('\u0E87', '\u0E88', state61),
				new Transition('\u1154', '\u1155', state61), new Transition('\u0A85', '\u0A8B', state61),
				new Transition('\u0C85', '\u0C8C', state61), new Transition('\u03A3', '\u03CE', state61),
				new Transition('\u0E8A', '\u0E8A', state61), new Transition('\u1FBE', '\u1FBE', state61),
				new Transition('\u04EE', '\u04F5', state61), new Transition('\u0559', '\u0559', state61),
				new Transition('\u1159', '\u1159', state61), new Transition('\u1F59', '\u1F59', state61),
				new Transition('\u3021', '\u3029', state61), new Transition('\u11BC', '\u11C2', state61),
				new Transition('\u0A8D', '\u0A8D', state61), new Transition('\u0E8D', '\u0E8D', state61),
				new Transition('\u1F5B', '\u1F5B', state61), new Transition('\u0C8E', '\u0C90', state61),
				new Transition('\u0F49', '\u0F69', state61), new Transition('\u0B5C', '\u0B5D', state61),
				new Transition('\u1FC2', '\u1FC4', state61), new Transition('\u0A8F', '\u0A91', state61),
				new Transition('\u1F5D', '\u1F5D', state61), new Transition('\u0958', '\u0961', state61),
				new Transition('\u04F8', '\u04F9', state61), new Transition('\u0B5F', '\u0B61', state61),
				new Transition('\u115F', '\u1161', state61), new Transition('\u0D60', '\u0D61', state61),
				new Transition('\u0A2A', '\u0A30', state61), new Transition('\uAC00', '\uD7A3', state61),
				new Transition('\u0C2A', '\u0C33', state61), new Transition('\u0E94', '\u0E97', state61),
				new Transition('\u4E00', '\u9FA5', state61), new Transition('\u1163', '\u1163', state61),
				new Transition('\u0621', '\u063A', state61), new Transition('\u0E30', '\u0E30', state61),
				new Transition('\u1FC6', '\u1FCC', state61), new Transition('\u1165', '\u1165', state61),
				new Transition('\u0A32', '\u0A33', state61), new Transition('\u0E32', '\u0E33', state61),
				new Transition('\u1100', '\u1100', state61), new Transition('\u1167', '\u1167', state61),
				new Transition('\u0671', '\u06B7', state61), new Transition('\u040E', '\u044F', state61),
				new Transition('\u0A35', '\u0A36', state61), new Transition('\u1102', '\u1103', state61),
				new Transition('\u1169', '\u1169', state61), new Transition('\u014A', '\u017E', state61),
				new Transition('\u0E99', '\u0E9F', state61), new Transition('\u0C35', '\u0C39', state61),
				new Transition('\u1FD0', '\u1FD3', state61), new Transition('\u0A38', '\u0A39', state61),
				new Transition('\u0C92', '\u0CA8', state61), new Transition('\u0A93', '\u0AA8', state61),
				new Transition('\u1105', '\u1107', state61), new Transition('\u03D0', '\u03D6', state61),
				new Transition('\u116D', '\u116E', state61), new Transition('\u0EA1', '\u0EA3', state61),
				new Transition('\u1109', '\u1109', state61), new Transition('\u0B05', '\u0B0C', state61),
				new Transition('\u0D05', '\u0D0C', state61), new Transition('\u1F5F', '\u1F7D', state61),
				new Transition('\u0EA5', '\u0EA5', state61), new Transition('\u110B', '\u110C', state61),
				new Transition('\u1172', '\u1173', state61), new Transition('\u1FD6', '\u1FDB', state61),
				new Transition('\u1F00', '\u1F15', state61), new Transition('\u03DA', '\u03DA', state61),
				new Transition('\u0EA7', '\u0EA7', state61), new Transition('\u1175', '\u1175', state61),
				new Transition('\u03DC', '\u03DC', state61), new Transition('\u0D0E', '\u0D10', state61),
				new Transition('\u0B0F', '\u0B10', state61), new Transition('\u09DC', '\u09DD', state61),
				new Transition('\u0E40', '\u0E45', state61), new Transition('\u110E', '\u1112', state61),
				new Transition('\u0561', '\u0586', state61), new Transition('\u0EAA', '\u0EAB', state61),
				new Transition('\u03DE', '\u03DE', state61), new Transition('\u05D0', '\u05EA', state61),
				new Transition('\u03E0', '\u03E0', state61), new Transition('\u0641', '\u064A', state61),
				new Transition('\u09DF', '\u09E1', state61), new Transition('\u0AAA', '\u0AB0', state61),
				new Transition('\u0EAD', '\u0EAE', state61), new Transition('\u01CD', '\u01F0', state61),
				new Transition('\u0490', '\u04C4', state61), new Transition('\u0CAA', '\u0CB3', state61),
				new Transition('\u0EB0', '\u0EB0', state61), new Transition('\u0AB2', '\u0AB3', state61),
				new Transition('\u0EB2', '\u0EB3', state61), new Transition('\u00F8', '\u0131', state61),
				new Transition('\u1FE0', '\u1FEC', state61), new Transition('\u2180', '\u2182', state61),
				new Transition('\u1F18', '\u1F1D', state61), new Transition('\u3105', '\u312C', state61),
				new Transition('\u10A0', '\u10C5', state61), new Transition('\u0041', '\u005A', state61),
				new Transition('\u0AB5', '\u0AB9', state61), new Transition('\u0CB5', '\u0CB9', state61),
				new Transition('\u11EB', '\u11EB', state61), new Transition('\u0D12', '\u0D28', state61),
				new Transition('\u03E2', '\u03F3', state61), new Transition('\u0386', '\u0386', state61),
				new Transition('\u0B13', '\u0B28', state61), new Transition('\u0B85', '\u0B8A', state61),
				new Transition('\u0388', '\u038A', state61), new Transition('\u06BA', '\u06BE', state61),
				new Transition('\u0985', '\u098C', state61), new Transition('\u11F0', '\u11F0', state61),
				new Transition('\u0ABD', '\u0ABD', state61), new Transition('\u0EBD', '\u0EBD', state61),
				new Transition('\u09F0', '\u09F1', state61), new Transition('\u0905', '\u0939', state61),
				new Transition('\u0451', '\u045C', state61), new Transition('\u05F0', '\u05F2', state61),
				new Transition('\u02BB', '\u02C1', state61), new Transition('\u038C', '\u038C', state61),
				new Transition('\u2126', '\u2126', state61), new Transition('\u1FF2', '\u1FF4', state61),
				new Transition('\u0A59', '\u0A5C', state61), new Transition('\u01F4', '\u01F5', state61),
				new Transition('\u0B8E', '\u0B90', state61), new Transition('\u0EC0', '\u0EC4', state61),
				new Transition('\u098F', '\u0990', state61), new Transition('\u1E00', '\u1E9B', state61),
				new Transition('\u212A', '\u212B', state61), new Transition('\u0A5E', '\u0A5E', state61),
				new Transition('\u005F', '\u005F', state61), new Transition('\u11F9', '\u11F9', state61),
				new Transition('\u1FF6', '\u1FFC', state61), new Transition('\u0B92', '\u0B95', state61),
				new Transition('\u0C60', '\u0C61', state61), new Transition('\u0B2A', '\u0B30', state61),
				new Transition('\u04C7', '\u04C8', state61), new Transition('\u212E', '\u212E', state61),
				new Transition('\u06C0', '\u06CE', state61), new Transition('\u04CB', '\u04CC', state61),
				new Transition('\u0B32', '\u0B33', state61), new Transition('\u038E', '\u03A1', state61),
				new Transition('\u0D2A', '\u0D39', state61));
		state46.alter(false, 45, new Transition('\u0041', '\u0046', state47),
				new Transition('\u0061', '\u0066', state47), new Transition('\u0030', '\u0039', state47));
		state47.alter(false, 39, new Transition('\u0021', '\u0021', state47),
				new Transition('\u003B', '\u003B', state47), new Transition('\u0030', '\u0039', state47),
				new Transition('\u002F', '\u002F', state23), new Transition('\u0040', '\u005A', state47),
				new Transition('\u0023', '\u0023', state60), new Transition('\u0061', '\u007A', state47),
				new Transition('\u007D', '\u007D', state59), new Transition('\u003D', '\u003D', state47),
				new Transition('\u0024', '\u0024', state47), new Transition('\u0026', '\u002E', state47),
				new Transition('\u007E', '\u007E', state47), new Transition('\u0025', '\u0025', state50),
				new Transition('\u005F', '\u005F', state47), new Transition('\u003F', '\u003F', state48));
		state48.alter(false, 42, new Transition('\u0061', '\u007A', state48),
				new Transition('\u007D', '\u007D', state59), new Transition('\u003D', '\u003D', state48),
				new Transition('\u005D', '\u005D', state48), new Transition('\u0024', '\u0024', state48),
				new Transition('\u0021', '\u0021', state48), new Transition('\u007E', '\u007E', state48),
				new Transition('\u0025', '\u0025', state33), new Transition('\u005F', '\u005F', state48),
				new Transition('\u0026', '\u003B', state48), new Transition('\u003F', '\u005B', state48),
				new Transition('\u0023', '\u0023', state60));
		state49.alter(false, 10, new Transition('\u0041', '\u0046', state23),
				new Transition('\u0061', '\u0066', state23), new Transition('\u0030', '\u0039', state23));
		state50.alter(false, 32, new Transition('\u0041', '\u0046', state46),
				new Transition('\u0061', '\u0066', state46), new Transition('\u0030', '\u0039', state46));
		state51.alter(false, 12, new Transition('\u0040', '\u0040', state47),
				new Transition('\u003A', '\u003A', state58), new Transition('\u002D', '\u002E', state51),
				new Transition('\u0021', '\u0021', state47), new Transition('\u003B', '\u003B', state47),
				new Transition('\u0026', '\u002A', state47), new Transition('\u0030', '\u0039', state51),
				new Transition('\u002F', '\u002F', state23), new Transition('\u0023', '\u0023', state60),
				new Transition('\u0041', '\u005A', state51), new Transition('\u0061', '\u007A', state51),
				new Transition('\u007D', '\u007D', state59), new Transition('\u003D', '\u003D', state47),
				new Transition('\u0024', '\u0024', state47), new Transition('\u007E', '\u007E', state47),
				new Transition('\u002B', '\u002B', state51), new Transition('\u0025', '\u0025', state50),
				new Transition('\u005F', '\u005F', state47), new Transition('\u003F', '\u003F', state48),
				new Transition('\u002C', '\u002C', state47));
		state52.alter(false, 50, new Transition('\u007D', '\u007D', state59),
				new Transition('\u003A', '\u003A', state56), new Transition('\u003F', '\u003F', state48),
				new Transition('\u002F', '\u002F', state23), new Transition('\u0023', '\u0023', state60));
		state53.alter(false, 43, new Transition('\u005D', '\u005D', state52),
				new Transition('\u0030', '\u0039', state37));
		state54.alter(false, 0, new Transition('\u0041', '\u0046', state60),
				new Transition('\u0061', '\u0066', state60), new Transition('\u0030', '\u0039', state60));
		state55.alter(false, 7, new Transition('\u0040', '\u0040', state47),
				new Transition('\u0021', '\u0021', state47), new Transition('\u003B', '\u003B', state47),
				new Transition('\u002F', '\u002F', state25), new Transition('\u0030', '\u0039', state47),
				new Transition('\u0023', '\u0023', state60), new Transition('\u0041', '\u005A', state51),
				new Transition('\u0061', '\u007A', state51), new Transition('\u007D', '\u007D', state59),
				new Transition('\u003D', '\u003D', state47), new Transition('\u0024', '\u0024', state47),
				new Transition('\u0026', '\u002E', state47), new Transition('\u007E', '\u007E', state47),
				new Transition('\u0025', '\u0025', state50), new Transition('\u005F', '\u005F', state47));
		state56.alter(false, 34, new Transition('\u007D', '\u007D', state59),
				new Transition('\u0030', '\u0039', state56), new Transition('\u003F', '\u003F', state48),
				new Transition('\u002F', '\u002F', state23), new Transition('\u0023', '\u0023', state60));
		state57.alter(false, 9, new Transition('\u0041', '\u0046', state54),
				new Transition('\u0061', '\u0066', state54), new Transition('\u0030', '\u0039', state54));
		state58.alter(false, 26, new Transition('\u0061', '\u007A', state48),
				new Transition('\u0030', '\u003B', state48), new Transition('\u003D', '\u003D', state48),
				new Transition('\u0024', '\u0024', state48), new Transition('\u0021', '\u0021', state48),
				new Transition('\u0026', '\u002E', state48), new Transition('\u007E', '\u007E', state48),
				new Transition('\u0025', '\u0025', state33), new Transition('\u002F', '\u002F', state25),
				new Transition('\u005F', '\u005F', state48), new Transition('\u003F', '\u005A', state48));
		state59.alter(false, 36, new Transition('\u0B99', '\u0B9A', state61),
				new Transition('\u00C0', '\u00D6', state61), new Transition('\u0B9C', '\u0B9C', state61),
				new Transition('\u1F20', '\u1F45', state61), new Transition('\u119E', '\u119E', state61),
				new Transition('\u0B36', '\u0B39', state61), new Transition('\u06D0', '\u06D3', state61),
				new Transition('\u0B9E', '\u0B9F', state61), new Transition('\u1F80', '\u1FB4', state61),
				new Transition('\u0993', '\u09A8', state61), new Transition('\u0134', '\u013E', state61),
				new Transition('\u3007', '\u3007', state61), new Transition('\u0401', '\u040C', state61),
				new Transition('\u0A05', '\u0A0A', state61), new Transition('\u06D5', '\u06D5', state61),
				new Transition('\u1EA0', '\u1EF9', state61), new Transition('\u113C', '\u113C', state61),
				new Transition('\u0C05', '\u0C0C', state61), new Transition('\u0061', '\u007A', state61),
				new Transition('\u30A1', '\u30FA', state61), new Transition('\u093D', '\u093D', state61),
				new Transition('\u0B3D', '\u0B3D', state61), new Transition('\u0BA3', '\u0BA4', state61),
				new Transition('\u113E', '\u113E', state61), new Transition('\u01FA', '\u0217', state61),
				new Transition('\u3041', '\u3094', state61), new Transition('\u045E', '\u0481', state61),
				new Transition('\u0A72', '\u0A74', state61), new Transition('\u1140', '\u1140', state61),
				new Transition('\u11A8', '\u11A8', state61), new Transition('\u0180', '\u01C3', state61),
				new Transition('\u0C0E', '\u0C10', state61), new Transition('\u0A0F', '\u0A10', state61),
				new Transition('\u0BA8', '\u0BAA', state61), new Transition('\u0F40', '\u0F47', state61),
				new Transition('\u0CDE', '\u0CDE', state61), new Transition('\u11AB', '\u11AB', state61),
				new Transition('\u0141', '\u0148', state61), new Transition('\u0AE0', '\u0AE0', state61),
				new Transition('\u04D0', '\u04EB', state61), new Transition('\u0CE0', '\u0CE1', state61),
				new Transition('\u0531', '\u0556', state61), new Transition('\u09AA', '\u09B0', state61),
				new Transition('\u11AE', '\u11AF', state61), new Transition('\u1F48', '\u1F4D', state61),
				new Transition('\u09B2', '\u09B2', state61), new Transition('\u0BAE', '\u0BB5', state61),
				new Transition('\u06E5', '\u06E6', state61), new Transition('\u114C', '\u114C', state61),
				new Transition('\u10D0', '\u10F6', state61), new Transition('\u114E', '\u114E', state61),
				new Transition('\u0E81', '\u0E82', state61), new Transition('\u0E01', '\u0E2E', state61),
				new Transition('\u1150', '\u1150', state61), new Transition('\u00D8', '\u00F6', state61),
				new Transition('\u0E84', '\u0E84', state61), new Transition('\u11B7', '\u11B8', state61),
				new Transition('\u09B6', '\u09B9', state61), new Transition('\u0250', '\u02A8', state61),
				new Transition('\u0BB7', '\u0BB9', state61), new Transition('\u0C12', '\u0C28', state61),
				new Transition('\u0A13', '\u0A28', state61), new Transition('\u1FB6', '\u1FBC', state61),
				new Transition('\u11BA', '\u11BA', state61), new Transition('\u1F50', '\u1F57', state61),
				new Transition('\u0E87', '\u0E88', state61), new Transition('\u1154', '\u1155', state61),
				new Transition('\u0A85', '\u0A8B', state61), new Transition('\u0C85', '\u0C8C', state61),
				new Transition('\u03A3', '\u03CE', state61), new Transition('\u0E8A', '\u0E8A', state61),
				new Transition('\u1FBE', '\u1FBE', state61), new Transition('\u04EE', '\u04F5', state61),
				new Transition('\u0559', '\u0559', state61), new Transition('\u1159', '\u1159', state61),
				new Transition('\u1F59', '\u1F59', state61), new Transition('\u3021', '\u3029', state61),
				new Transition('\u11BC', '\u11C2', state61), new Transition('\u0A8D', '\u0A8D', state61),
				new Transition('\u0E8D', '\u0E8D', state61), new Transition('\u1F5B', '\u1F5B', state61),
				new Transition('\u0C8E', '\u0C90', state61), new Transition('\u0F49', '\u0F69', state61),
				new Transition('\u0B5C', '\u0B5D', state61), new Transition('\u1FC2', '\u1FC4', state61),
				new Transition('\u0A8F', '\u0A91', state61), new Transition('\u1F5D', '\u1F5D', state61),
				new Transition('\u0958', '\u0961', state61), new Transition('\u04F8', '\u04F9', state61),
				new Transition('\u0B5F', '\u0B61', state61), new Transition('\u115F', '\u1161', state61),
				new Transition('\u0D60', '\u0D61', state61), new Transition('\u0A2A', '\u0A30', state61),
				new Transition('\uAC00', '\uD7A3', state61), new Transition('\u0C2A', '\u0C33', state61),
				new Transition('\u0E94', '\u0E97', state61), new Transition('\u4E00', '\u9FA5', state61),
				new Transition('\u1163', '\u1163', state61), new Transition('\u0621', '\u063A', state61),
				new Transition('\u0E30', '\u0E30', state61), new Transition('\u1FC6', '\u1FCC', state61),
				new Transition('\u1165', '\u1165', state61), new Transition('\u0A32', '\u0A33', state61),
				new Transition('\u0E32', '\u0E33', state61), new Transition('\u1100', '\u1100', state61),
				new Transition('\u1167', '\u1167', state61), new Transition('\u0671', '\u06B7', state61),
				new Transition('\u040E', '\u044F', state61), new Transition('\u0A35', '\u0A36', state61),
				new Transition('\u1102', '\u1103', state61), new Transition('\u1169', '\u1169', state61),
				new Transition('\u014A', '\u017E', state61), new Transition('\u0E99', '\u0E9F', state61),
				new Transition('\u0C35', '\u0C39', state61), new Transition('\u1FD0', '\u1FD3', state61),
				new Transition('\u0A38', '\u0A39', state61), new Transition('\u0C92', '\u0CA8', state61),
				new Transition('\u0A93', '\u0AA8', state61), new Transition('\u1105', '\u1107', state61),
				new Transition('\u03D0', '\u03D6', state61), new Transition('\u116D', '\u116E', state61),
				new Transition('\u0EA1', '\u0EA3', state61), new Transition('\u1109', '\u1109', state61),
				new Transition('\u0B05', '\u0B0C', state61), new Transition('\u0D05', '\u0D0C', state61),
				new Transition('\u1F5F', '\u1F7D', state61), new Transition('\u0EA5', '\u0EA5', state61),
				new Transition('\u110B', '\u110C', state61), new Transition('\u1172', '\u1173', state61),
				new Transition('\u1FD6', '\u1FDB', state61), new Transition('\u1F00', '\u1F15', state61),
				new Transition('\u03DA', '\u03DA', state61), new Transition('\u0EA7', '\u0EA7', state61),
				new Transition('\u1175', '\u1175', state61), new Transition('\u03DC', '\u03DC', state61),
				new Transition('\u0D0E', '\u0D10', state61), new Transition('\u0B0F', '\u0B10', state61),
				new Transition('\u09DC', '\u09DD', state61), new Transition('\u0E40', '\u0E45', state61),
				new Transition('\u110E', '\u1112', state61), new Transition('\u0561', '\u0586', state61),
				new Transition('\u0EAA', '\u0EAB', state61), new Transition('\u03DE', '\u03DE', state61),
				new Transition('\u05D0', '\u05EA', state61), new Transition('\u03E0', '\u03E0', state61),
				new Transition('\u0641', '\u064A', state61), new Transition('\u09DF', '\u09E1', state61),
				new Transition('\u0AAA', '\u0AB0', state61), new Transition('\u0EAD', '\u0EAE', state61),
				new Transition('\u01CD', '\u01F0', state61), new Transition('\u0490', '\u04C4', state61),
				new Transition('\u0CAA', '\u0CB3', state61), new Transition('\u0EB0', '\u0EB0', state61),
				new Transition('\u0AB2', '\u0AB3', state61), new Transition('\u0EB2', '\u0EB3', state61),
				new Transition('\u00F8', '\u0131', state61), new Transition('\u1FE0', '\u1FEC', state61),
				new Transition('\u2180', '\u2182', state61), new Transition('\u1F18', '\u1F1D', state61),
				new Transition('\u3105', '\u312C', state61), new Transition('\u10A0', '\u10C5', state61),
				new Transition('\u0041', '\u005A', state61), new Transition('\u0AB5', '\u0AB9', state61),
				new Transition('\u0CB5', '\u0CB9', state61), new Transition('\u11EB', '\u11EB', state61),
				new Transition('\u0D12', '\u0D28', state61), new Transition('\u03E2', '\u03F3', state61),
				new Transition('\u0386', '\u0386', state61), new Transition('\u0B13', '\u0B28', state61),
				new Transition('\u0B85', '\u0B8A', state61), new Transition('\u0388', '\u038A', state61),
				new Transition('\u06BA', '\u06BE', state61), new Transition('\u0985', '\u098C', state61),
				new Transition('\u11F0', '\u11F0', state61), new Transition('\u0ABD', '\u0ABD', state61),
				new Transition('\u0EBD', '\u0EBD', state61), new Transition('\u09F0', '\u09F1', state61),
				new Transition('\u0905', '\u0939', state61), new Transition('\u0451', '\u045C', state61),
				new Transition('\u05F0', '\u05F2', state61), new Transition('\u02BB', '\u02C1', state61),
				new Transition('\u038C', '\u038C', state61), new Transition('\u2126', '\u2126', state61),
				new Transition('\u1FF2', '\u1FF4', state61), new Transition('\u0A59', '\u0A5C', state61),
				new Transition('\u01F4', '\u01F5', state61), new Transition('\u0B8E', '\u0B90', state61),
				new Transition('\u0EC0', '\u0EC4', state61), new Transition('\u098F', '\u0990', state61),
				new Transition('\u1E00', '\u1E9B', state61), new Transition('\u212A', '\u212B', state61),
				new Transition('\u0A5E', '\u0A5E', state61), new Transition('\u005F', '\u005F', state61),
				new Transition('\u11F9', '\u11F9', state61), new Transition('\u1FF6', '\u1FFC', state61),
				new Transition('\u0B92', '\u0B95', state61), new Transition('\u0C60', '\u0C61', state61),
				new Transition('\u0B2A', '\u0B30', state61), new Transition('\u04C7', '\u04C8', state61),
				new Transition('\u212E', '\u212E', state61), new Transition('\u06C0', '\u06CE', state61),
				new Transition('\u04CB', '\u04CC', state61), new Transition('\u0B32', '\u0B33', state61),
				new Transition('\u038E', '\u03A1', state61), new Transition('\u0D2A', '\u0D39', state61));
		state60.alter(false, 44, new Transition('\u0061', '\u007A', state60),
				new Transition('\u007D', '\u007D', state59), new Transition('\u003D', '\u003D', state60),
				new Transition('\u005D', '\u005D', state60), new Transition('\u0024', '\u0024', state60),
				new Transition('\u0021', '\u0021', state60), new Transition('\u007E', '\u007E', state60),
				new Transition('\u0025', '\u0025', state57), new Transition('\u005F', '\u005F', state60),
				new Transition('\u0026', '\u003B', state60), new Transition('\u003F', '\u005B', state60));
		state61.alter(true, 24, new Transition('\u0B99', '\u0B9A', state61),
				new Transition('\u00C0', '\u00D6', state61), new Transition('\u0591', '\u05A1', state61),
				new Transition('\u0F35', '\u0F35', state61), new Transition('\u0A02', '\u0A02', state61),
				new Transition('\u0C01', '\u0C03', state61), new Transition('\u0B9C', '\u0B9C', state61),
				new Transition('\u1F20', '\u1F45', state61), new Transition('\u02D0', '\u02D1', state61),
				new Transition('\u0F37', '\u0F37', state61), new Transition('\u119E', '\u119E', state61),
				new Transition('\u0B36', '\u0B39', state61), new Transition('\u06D0', '\u06D3', state61),
				new Transition('\u0B9E', '\u0B9F', state61), new Transition('\u0C66', '\u0C6F', state61),
				new Transition('\u3005', '\u3005', state61), new Transition('\u1F80', '\u1FB4', state61),
				new Transition('\u0F39', '\u0F39', state61), new Transition('\u0993', '\u09A8', state61),
				new Transition('\u0134', '\u013E', state61), new Transition('\u3007', '\u3007', state61),
				new Transition('\u0401', '\u040C', state61), new Transition('\u0A05', '\u0A0A', state61),
				new Transition('\u0A66', '\u0A74', state61), new Transition('\u0ED0', '\u0ED9', state61),
				new Transition('\u1EA0', '\u1EF9', state61), new Transition('\u0CD5', '\u0CD6', state61),
				new Transition('\u113C', '\u113C', state61), new Transition('\u0C05', '\u0C0C', state61),
				new Transition('\u0061', '\u007A', state61), new Transition('\u30A1', '\u30FA', state61),
				new Transition('\u0BA3', '\u0BA4', state61), new Transition('\u20D0', '\u20DC', state61),
				new Transition('\u113E', '\u113E', state61), new Transition('\u01FA', '\u0217', state61),
				new Transition('\u0F99', '\u0FAD', state61), new Transition('\u3041', '\u3094', state61),
				new Transition('\u045E', '\u0481', state61), new Transition('\u1140', '\u1140', state61),
				new Transition('\u0B3C', '\u0B43', state61), new Transition('\u0D3E', '\u0D43', state61),
				new Transition('\u11A8', '\u11A8', state61), new Transition('\u0180', '\u01C3', state61),
				new Transition('\u0C0E', '\u0C10', state61), new Transition('\u0A0F', '\u0A10', state61),
				new Transition('\u0BA8', '\u0BAA', state61), new Transition('\u0F3E', '\u0F47', state61),
				new Transition('\u0CDE', '\u0CDE', state61), new Transition('\u11AB', '\u11AB', state61),
				new Transition('\u0141', '\u0148', state61), new Transition('\u093C', '\u094D', state61),
				new Transition('\u0AE0', '\u0AE0', state61), new Transition('\u04D0', '\u04EB', state61),
				new Transition('\u06D5', '\u06E8', state61), new Transition('\u0CE0', '\u0CE1', state61),
				new Transition('\u0531', '\u0556', state61), new Transition('\u09AA', '\u09B0', state61),
				new Transition('\u0D46', '\u0D48', state61), new Transition('\u20E1', '\u20E1', state61),
				new Transition('\u0B47', '\u0B48', state61), new Transition('\u11AE', '\u11AF', state61),
				new Transition('\u05A3', '\u05B9', state61), new Transition('\u1F48', '\u1F4D', state61),
				new Transition('\u09B2', '\u09B2', state61), new Transition('\u0BAE', '\u0BB5', state61),
				new Transition('\u0D4A', '\u0D4D', state61), new Transition('\u114C', '\u114C', state61),
				new Transition('\u0B4B', '\u0B4D', state61), new Transition('\u10D0', '\u10F6', state61),
				new Transition('\u114E', '\u114E', state61), new Transition('\u0FB1', '\u0FB7', state61),
				new Transition('\u0E81', '\u0E82', state61), new Transition('\u0A81', '\u0A83', state61),
				new Transition('\u0E01', '\u0E2E', state61), new Transition('\u0C82', '\u0C83', state61),
				new Transition('\u1150', '\u1150', state61), new Transition('\u00D8', '\u00F6', state61),
				new Transition('\u0E84', '\u0E84', state61), new Transition('\u11B7', '\u11B8', state61),
				new Transition('\u09B6', '\u09B9', state61), new Transition('\u0250', '\u02A8', state61),
				new Transition('\u0483', '\u0486', state61), new Transition('\u0AE6', '\u0AEF', state61),
				new Transition('\u0BB7', '\u0BB9', state61), new Transition('\u0CE6', '\u0CEF', state61),
				new Transition('\u06EA', '\u06ED', state61), new Transition('\u0C12', '\u0C28', state61),
				new Transition('\u0FB9', '\u0FB9', state61), new Transition('\u0951', '\u0954', state61),
				new Transition('\u0A13', '\u0A28', state61), new Transition('\u1FB6', '\u1FBC', state61),
				new Transition('\u11BA', '\u11BA', state61), new Transition('\u1F50', '\u1F57', state61),
				new Transition('\u0E87', '\u0E88', state61), new Transition('\u1154', '\u1155', state61),
				new Transition('\u0A85', '\u0A8B', state61), new Transition('\u09BC', '\u09BC', state61),
				new Transition('\u05BB', '\u05BD', state61), new Transition('\u0C85', '\u0C8C', state61),
				new Transition('\u03A3', '\u03CE', state61), new Transition('\u0B56', '\u0B57', state61),
				new Transition('\u0E8A', '\u0E8A', state61), new Transition('\u0D57', '\u0D57', state61),
				new Transition('\u1FBE', '\u1FBE', state61), new Transition('\u04EE', '\u04F5', state61),
				new Transition('\u05BF', '\u05BF', state61), new Transition('\u0559', '\u0559', state61),
				new Transition('\u1159', '\u1159', state61), new Transition('\u1F59', '\u1F59', state61),
				new Transition('\u11BC', '\u11C2', state61), new Transition('\u0A8D', '\u0A8D', state61),
				new Transition('\u0E8D', '\u0E8D', state61), new Transition('\u0BBE', '\u0BC2', state61),
				new Transition('\u1F5B', '\u1F5B', state61), new Transition('\u05C1', '\u05C2', state61),
				new Transition('\u09BE', '\u09C4', state61), new Transition('\u06F0', '\u06F9', state61),
				new Transition('\u0C8E', '\u0C90', state61), new Transition('\u0F49', '\u0F69', state61),
				new Transition('\u0B5C', '\u0B5D', state61), new Transition('\u3021', '\u302F', state61),
				new Transition('\u1FC2', '\u1FC4', state61), new Transition('\u0A8F', '\u0A91', state61),
				new Transition('\u1F5D', '\u1F5D', state61), new Transition('\u05C4', '\u05C4', state61),
				new Transition('\u0958', '\u0963', state61), new Transition('\u04F8', '\u04F9', state61),
				new Transition('\u0B5F', '\u0B61', state61), new Transition('\u115F', '\u1161', state61),
				new Transition('\u0360', '\u0361', state61), new Transition('\u0D60', '\u0D61', state61),
				new Transition('\u002D', '\u002E', state61), new Transition('\u0A2A', '\u0A30', state61),
				new Transition('\u0BC6', '\u0BC8', state61), new Transition('\u09C7', '\u09C8', state61),
				new Transition('\uAC00', '\uD7A3', state61), new Transition('\u0C2A', '\u0C33', state61),
				new Transition('\u0E94', '\u0E97', state61), new Transition('\u4E00', '\u9FA5', state61),
				new Transition('\u1163', '\u1163', state61), new Transition('\u0621', '\u063A', state61),
				new Transition('\u1FC6', '\u1FCC', state61), new Transition('\u30FC', '\u30FE', state61),
				new Transition('\u1165', '\u1165', state61), new Transition('\u0BCA', '\u0BCD', state61),
				new Transition('\u09CB', '\u09CD', state61), new Transition('\u0A32', '\u0A33', state61),
				new Transition('\u1100', '\u1100', state61), new Transition('\u3099', '\u309A', state61),
				new Transition('\u3031', '\u3035', state61), new Transition('\u1167', '\u1167', state61),
				new Transition('\u0670', '\u06B7', state61), new Transition('\u040E', '\u044F', state61),
				new Transition('\u0030', '\u0039', state61), new Transition('\u0901', '\u0903', state61),
				new Transition('\u0B01', '\u0B03', state61), new Transition('\u0A35', '\u0A36', state61),
				new Transition('\u0D02', '\u0D03', state61), new Transition('\u1102', '\u1103', state61),
				new Transition('\u1169', '\u1169', state61), new Transition('\u014A', '\u017E', state61),
				new Transition('\u0E30', '\u0E3A', state61), new Transition('\u0E99', '\u0E9F', state61),
				new Transition('\u309D', '\u309E', state61), new Transition('\u0C35', '\u0C39', state61),
				new Transition('\u0966', '\u096F', state61), new Transition('\u0B66', '\u0B6F', state61),
				new Transition('\u0D66', '\u0D6F', state61), new Transition('\u1FD0', '\u1FD3', state61),
				new Transition('\u0A38', '\u0A39', state61), new Transition('\u0C92', '\u0CA8', state61),
				new Transition('\u0A93', '\u0AA8', state61), new Transition('\u1105', '\u1107', state61),
				new Transition('\u03D0', '\u03D6', state61), new Transition('\u116D', '\u116E', state61),
				new Transition('\u0EA1', '\u0EA3', state61), new Transition('\u0A3C', '\u0A3C', state61),
				new Transition('\u1109', '\u1109', state61), new Transition('\u0B05', '\u0B0C', state61),
				new Transition('\u0D05', '\u0D0C', state61), new Transition('\u09D7', '\u09D7', state61),
				new Transition('\u0BD7', '\u0BD7', state61), new Transition('\u1F5F', '\u1F7D', state61),
				new Transition('\u0EA5', '\u0EA5', state61), new Transition('\u110B', '\u110C', state61),
				new Transition('\u1172', '\u1173', state61), new Transition('\u1FD6', '\u1FDB', state61),
				new Transition('\u1F00', '\u1F15', state61), new Transition('\u03DA', '\u03DA', state61),
				new Transition('\u0A3E', '\u0A42', state61), new Transition('\u0EA7', '\u0EA7', state61),
				new Transition('\u0C3E', '\u0C44', state61), new Transition('\u1175', '\u1175', state61),
				new Transition('\u03DC', '\u03DC', state61), new Transition('\u0D0E', '\u0D10', state61),
				new Transition('\u0B0F', '\u0B10', state61), new Transition('\u09DC', '\u09DD', state61),
				new Transition('\u110E', '\u1112', state61), new Transition('\u0561', '\u0586', state61),
				new Transition('\u0EAA', '\u0EAB', state61), new Transition('\u03DE', '\u03DE', state61),
				new Transition('\u05D0', '\u05EA', state61), new Transition('\u03E0', '\u03E0', state61),
				new Transition('\u0AAA', '\u0AB0', state61), new Transition('\u0C46', '\u0C48', state61),
				new Transition('\u0EAD', '\u0EAE', state61), new Transition('\u0A47', '\u0A48', state61),
				new Transition('\u09DF', '\u09E3', state61), new Transition('\u01CD', '\u01F0', state61),
				new Transition('\u0E40', '\u0E4E', state61), new Transition('\u0490', '\u04C4', state61),
				new Transition('\u0CAA', '\u0CB3', state61), new Transition('\u0F71', '\u0F84', state61),
				new Transition('\u0640', '\u0652', state61), new Transition('\u0C4A', '\u0C4D', state61),
				new Transition('\u0F18', '\u0F19', state61), new Transition('\u0A4B', '\u0A4D', state61),
				new Transition('\u0AB2', '\u0AB3', state61), new Transition('\u00F8', '\u0131', state61),
				new Transition('\u1FE0', '\u1FEC', state61), new Transition('\u2180', '\u2182', state61),
				new Transition('\u1F18', '\u1F1D', state61), new Transition('\u0981', '\u0983', state61),
				new Transition('\u0EB0', '\u0EB9', state61), new Transition('\u0B82', '\u0B83', state61),
				new Transition('\u3105', '\u312C', state61), new Transition('\u10A0', '\u10C5', state61),
				new Transition('\u0041', '\u005A', state61), new Transition('\u00B7', '\u00B7', state61),
				new Transition('\u0AB5', '\u0AB9', state61), new Transition('\u0CB5', '\u0CB9', state61),
				new Transition('\u11EB', '\u11EB', state61), new Transition('\u0BE7', '\u0BEF', state61),
				new Transition('\u0D12', '\u0D28', state61), new Transition('\u03E2', '\u03F3', state61),
				new Transition('\u0B13', '\u0B28', state61), new Transition('\u09E6', '\u09F1', state61),
				new Transition('\u0B85', '\u0B8A', state61), new Transition('\u0386', '\u038A', state61),
				new Transition('\u0E50', '\u0E59', state61), new Transition('\u0C55', '\u0C56', state61),
				new Transition('\u0EBB', '\u0EBD', state61), new Transition('\u0F86', '\u0F8B', state61),
				new Transition('\u06BA', '\u06BE', state61), new Transition('\u0985', '\u098C', state61),
				new Transition('\u11F0', '\u11F0', state61), new Transition('\u0905', '\u0939', state61),
				new Transition('\u0451', '\u045C', state61), new Transition('\u05F0', '\u05F2', state61),
				new Transition('\u02BB', '\u02C1', state61), new Transition('\u0F20', '\u0F29', state61),
				new Transition('\u038C', '\u038C', state61), new Transition('\u2126', '\u2126', state61),
				new Transition('\u1FF2', '\u1FF4', state61), new Transition('\u0A59', '\u0A5C', state61),
				new Transition('\u01F4', '\u01F5', state61), new Transition('\u0ABC', '\u0AC5', state61),
				new Transition('\u0CBE', '\u0CC4', state61), new Transition('\u0B8E', '\u0B90', state61),
				new Transition('\u0EC0', '\u0EC4', state61), new Transition('\u098F', '\u0990', state61),
				new Transition('\u0300', '\u0345', state61), new Transition('\u1E00', '\u1E9B', state61),
				new Transition('\u212A', '\u212B', state61), new Transition('\u0A5E', '\u0A5E', state61),
				new Transition('\u005F', '\u005F', state61), new Transition('\u11F9', '\u11F9', state61),
				new Transition('\u0EC6', '\u0EC6', state61), new Transition('\u0F90', '\u0F95', state61),
				new Transition('\u1FF6', '\u1FFC', state61), new Transition('\u0B92', '\u0B95', state61),
				new Transition('\u0C60', '\u0C61', state61), new Transition('\u0B2A', '\u0B30', state61),
				new Transition('\u0CC6', '\u0CC8', state61), new Transition('\u04C7', '\u04C8', state61),
				new Transition('\u212E', '\u212E', state61), new Transition('\u0AC7', '\u0AC9', state61),
				new Transition('\u06C0', '\u06CE', state61), new Transition('\u0F97', '\u0F97', state61),
				new Transition('\u0EC8', '\u0ECD', state61), new Transition('\u04CB', '\u04CC', state61),
				new Transition('\u0660', '\u0669', state61), new Transition('\u0CCA', '\u0CCD', state61),
				new Transition('\u0ACB', '\u0ACD', state61), new Transition('\u0B32', '\u0B33', state61),
				new Transition('\u038E', '\u03A1', state61), new Transition('\u0D2A', '\u0D39', state61));
		return Automaton.load(state45, true, null);
	}

	private static Automaton getAut151() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		state0.alter(true, 15, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state2), new Transition('\u0061', '\u007A', state2),
				new Transition('\r', '\r', state15), new Transition('\u002B', '\u002B', state2),
				new Transition('\u002F', '\u0039', state2));
		state1.alter(true, 8, new Transition('\u0009', '\n', state15), new Transition('\u0041', '\u005A', state2),
				new Transition('\u0061', '\u007A', state2), new Transition('\u0020', '\u0020', state0),
				new Transition('\r', '\r', state15), new Transition('\u002B', '\u002B', state2),
				new Transition('\u002F', '\u0039', state2));
		state2.alter(false, 5, new Transition('\u0020', '\u0020', state8), new Transition('\u0052', '\u005A', state3),
				new Transition('\u0068', '\u0076', state3), new Transition('\u0067', '\u0067', state5),
				new Transition('\u0077', '\u0077', state5), new Transition('\u0042', '\u0050', state3),
				new Transition('\u0061', '\u0066', state3), new Transition('\u0041', '\u0041', state5),
				new Transition('\u0051', '\u0051', state5), new Transition('\u002B', '\u002B', state3),
				new Transition('\u002F', '\u0039', state3), new Transition('\u0078', '\u007A', state3));
		state3.alter(false, 11, new Transition('\u004D', '\u004D', state7), new Transition('\u005A', '\u005A', state10),
				new Transition('\u0067', '\u0067', state7), new Transition('\u0034', '\u0034', state7),
				new Transition('\u0041', '\u0041', state7), new Transition('\u0074', '\u0076', state10),
				new Transition('\u004E', '\u0050', state10), new Transition('\u0068', '\u006A', state10),
				new Transition('\u0035', '\u0037', state10), new Transition('\u0042', '\u0044', state10),
				new Transition('\u0077', '\u0077', state7), new Transition('\u0051', '\u0051', state7),
				new Transition('\u002B', '\u002B', state10), new Transition('\u006B', '\u006B', state7),
				new Transition('\u0038', '\u0038', state7), new Transition('\u0045', '\u0045', state7),
				new Transition('\u0039', '\u0039', state10), new Transition('\u0078', '\u007A', state10),
				new Transition('\u0052', '\u0054', state10), new Transition('\u0020', '\u0020', state6),
				new Transition('\u006C', '\u006E', state10), new Transition('\u0046', '\u0048', state10),
				new Transition('\u0061', '\u0062', state10), new Transition('\u0055', '\u0055', state7),
				new Transition('\u002F', '\u002F', state10), new Transition('\u006F', '\u006F', state7),
				new Transition('\u0049', '\u0049', state7), new Transition('\u0063', '\u0063', state7),
				new Transition('\u0030', '\u0030', state7), new Transition('\u0056', '\u0058', state10),
				new Transition('\u0070', '\u0072', state10), new Transition('\u004A', '\u004C', state10),
				new Transition('\u0064', '\u0066', state10), new Transition('\u0031', '\u0033', state10),
				new Transition('\u0059', '\u0059', state7), new Transition('\u0073', '\u0073', state7));
		state4.alter(true, 2, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\u0041', '\u005A', state2), new Transition('\u0061', '\u007A', state2),
				new Transition('\r', '\r', state4), new Transition('\u002B', '\u002B', state2),
				new Transition('\u002F', '\u0039', state2));
		state5.alter(false, 6, new Transition('\u004D', '\u004D', state7), new Transition('\u005A', '\u005A', state10),
				new Transition('\u0067', '\u0067', state7), new Transition('\u0034', '\u0034', state7),
				new Transition('\u0041', '\u0041', state7), new Transition('\u0074', '\u0076', state10),
				new Transition('\u004E', '\u0050', state10), new Transition('\u0068', '\u006A', state10),
				new Transition('\u0035', '\u0037', state10), new Transition('\u0042', '\u0044', state10),
				new Transition('\u0077', '\u0077', state7), new Transition('\u0051', '\u0051', state7),
				new Transition('\u002B', '\u002B', state10), new Transition('\u006B', '\u006B', state7),
				new Transition('\u0038', '\u0038', state7), new Transition('\u0045', '\u0045', state7),
				new Transition('\u0039', '\u0039', state10), new Transition('\u0078', '\u007A', state10),
				new Transition('\u0052', '\u0054', state10), new Transition('\u0020', '\u0020', state14),
				new Transition('\u006C', '\u006E', state10), new Transition('\u0046', '\u0048', state10),
				new Transition('\u0061', '\u0062', state10), new Transition('\u0055', '\u0055', state7),
				new Transition('\u002F', '\u002F', state10), new Transition('\u006F', '\u006F', state7),
				new Transition('\u0049', '\u0049', state7), new Transition('\u0063', '\u0063', state7),
				new Transition('\u0030', '\u0030', state7), new Transition('\u003D', '\u003D', state9),
				new Transition('\u0056', '\u0058', state10), new Transition('\u0070', '\u0072', state10),
				new Transition('\u004A', '\u004C', state10), new Transition('\u0064', '\u0066', state10),
				new Transition('\u0031', '\u0033', state10), new Transition('\u0059', '\u0059', state7),
				new Transition('\u0073', '\u0073', state7));
		state6.alter(false, 1, new Transition('\u004D', '\u004D', state7), new Transition('\u005A', '\u005A', state10),
				new Transition('\u0067', '\u0067', state7), new Transition('\u0034', '\u0034', state7),
				new Transition('\u0041', '\u0041', state7), new Transition('\u0074', '\u0076', state10),
				new Transition('\u004E', '\u0050', state10), new Transition('\u0068', '\u006A', state10),
				new Transition('\u0035', '\u0037', state10), new Transition('\u0042', '\u0044', state10),
				new Transition('\u0077', '\u0077', state7), new Transition('\u0051', '\u0051', state7),
				new Transition('\u002B', '\u002B', state10), new Transition('\u006B', '\u006B', state7),
				new Transition('\u0038', '\u0038', state7), new Transition('\u0045', '\u0045', state7),
				new Transition('\u0039', '\u0039', state10), new Transition('\u0078', '\u007A', state10),
				new Transition('\u0052', '\u0054', state10), new Transition('\u006C', '\u006E', state10),
				new Transition('\u0046', '\u0048', state10), new Transition('\u0061', '\u0062', state10),
				new Transition('\u0055', '\u0055', state7), new Transition('\u002F', '\u002F', state10),
				new Transition('\u006F', '\u006F', state7), new Transition('\u0049', '\u0049', state7),
				new Transition('\u0063', '\u0063', state7), new Transition('\u0030', '\u0030', state7),
				new Transition('\u0056', '\u0058', state10), new Transition('\u0070', '\u0072', state10),
				new Transition('\u004A', '\u004C', state10), new Transition('\u0064', '\u0066', state10),
				new Transition('\u0031', '\u0033', state10), new Transition('\u0059', '\u0059', state7),
				new Transition('\u0073', '\u0073', state7));
		state7.alter(false, 3, new Transition('\u0041', '\u005A', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u0020', '\u0020', state11), new Transition('\u003D', '\u003D', state15),
				new Transition('\u002B', '\u002B', state1), new Transition('\u002F', '\u0039', state1));
		state8.alter(false, 12, new Transition('\u0052', '\u005A', state3), new Transition('\u0068', '\u0076', state3),
				new Transition('\u0067', '\u0067', state5), new Transition('\u0077', '\u0077', state5),
				new Transition('\u0042', '\u0050', state3), new Transition('\u0061', '\u0066', state3),
				new Transition('\u0041', '\u0041', state5), new Transition('\u0051', '\u0051', state5),
				new Transition('\u002B', '\u002B', state3), new Transition('\u002F', '\u0039', state3),
				new Transition('\u0078', '\u007A', state3));
		state9.alter(false, 10, new Transition('\u0020', '\u0020', state12),
				new Transition('\u003D', '\u003D', state15));
		state10.alter(false, 0, new Transition('\u0041', '\u005A', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u0020', '\u0020', state13), new Transition('\u002B', '\u002B', state1),
				new Transition('\u002F', '\u0039', state1));
		state11.alter(false, 9, new Transition('\u0041', '\u005A', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u003D', '\u003D', state15), new Transition('\u002B', '\u002B', state1),
				new Transition('\u002F', '\u0039', state1));
		state12.alter(false, 7, new Transition('\u003D', '\u003D', state15));
		state13.alter(false, 13, new Transition('\u0041', '\u005A', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u002B', '\u002B', state1), new Transition('\u002F', '\u0039', state1));
		state14.alter(false, 14, new Transition('\u004D', '\u004D', state7),
				new Transition('\u005A', '\u005A', state10), new Transition('\u0067', '\u0067', state7),
				new Transition('\u0034', '\u0034', state7), new Transition('\u0041', '\u0041', state7),
				new Transition('\u0074', '\u0076', state10), new Transition('\u004E', '\u0050', state10),
				new Transition('\u0068', '\u006A', state10), new Transition('\u0035', '\u0037', state10),
				new Transition('\u0042', '\u0044', state10), new Transition('\u0077', '\u0077', state7),
				new Transition('\u0051', '\u0051', state7), new Transition('\u002B', '\u002B', state10),
				new Transition('\u006B', '\u006B', state7), new Transition('\u0038', '\u0038', state7),
				new Transition('\u0045', '\u0045', state7), new Transition('\u0039', '\u0039', state10),
				new Transition('\u0078', '\u007A', state10), new Transition('\u0052', '\u0054', state10),
				new Transition('\u006C', '\u006E', state10), new Transition('\u0046', '\u0048', state10),
				new Transition('\u0061', '\u0062', state10), new Transition('\u0055', '\u0055', state7),
				new Transition('\u002F', '\u002F', state10), new Transition('\u006F', '\u006F', state7),
				new Transition('\u0049', '\u0049', state7), new Transition('\u0063', '\u0063', state7),
				new Transition('\u0030', '\u0030', state7), new Transition('\u003D', '\u003D', state9),
				new Transition('\u0056', '\u0058', state10), new Transition('\u0070', '\u0072', state10),
				new Transition('\u004A', '\u004C', state10), new Transition('\u0064', '\u0066', state10),
				new Transition('\u0031', '\u0033', state10), new Transition('\u0059', '\u0059', state7),
				new Transition('\u0073', '\u0073', state7));
		state15.alter(true, 4, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\r', '\r', state15));
		return Automaton.load(state4, true, null);
	}

	private static Automaton getAut152() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		state0.alter(false, 5, new Transition('\u0072', '\u0072', state3));
		state1.alter(false, 6, new Transition('\u0061', '\u0061', state2));
		state2.alter(false, 2, new Transition('\u006C', '\u006C', state6));
		state3.alter(false, 7, new Transition('\u0075', '\u0075', state4));
		state4.alter(false, 1, new Transition('\u0065', '\u0065', state7));
		state5.alter(false, 0, new Transition('\u0009', '\n', state5), new Transition('\u0020', '\u0020', state5),
				new Transition('\r', '\r', state5), new Transition('\u0030', '\u0031', state7),
				new Transition('\u0074', '\u0074', state0), new Transition('\u0066', '\u0066', state1));
		state6.alter(false, 4, new Transition('\u0073', '\u0073', state4));
		state7.alter(true, 3, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7));
		return Automaton.load(state5, true, null);
	}

	private static Automaton getAut153() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		state0.alter(true, 9, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\u0030', '\u0030', state0), new Transition('\r', '\r', state7),
				new Transition('\u0031', '\u0031', state2), new Transition('\u0032', '\u0039', state6));
		state1.alter(true, 7, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7), new Transition('\u0033', '\u0039', state7),
				new Transition('\u0030', '\u0031', state6), new Transition('\u0032', '\u0032', state4));
		state2.alter(true, 4, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7), new Transition('\u0033', '\u0039', state7),
				new Transition('\u0030', '\u0031', state6), new Transition('\u0032', '\u0032', state3));
		state3.alter(true, 5, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7), new Transition('\u0030', '\u0038', state7));
		state4.alter(true, 2, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7), new Transition('\u0030', '\u0037', state7));
		state5.alter(false, 3, new Transition('\u0030', '\u0030', state9), new Transition('\u0009', '\n', state5),
				new Transition('\u0020', '\u0020', state5), new Transition('\r', '\r', state5),
				new Transition('\u002D', '\u002D', state8), new Transition('\u0031', '\u0031', state1),
				new Transition('\u0032', '\u0039', state6));
		state6.alter(true, 1, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7), new Transition('\u0030', '\u0039', state7));
		state7.alter(true, 8, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\r', '\r', state7));
		state8.alter(false, 6, new Transition('\u0030', '\u0030', state0), new Transition('\u0031', '\u0031', state2),
				new Transition('\u0032', '\u0039', state6));
		state9.alter(true, 0, new Transition('\u0030', '\u0030', state9), new Transition('\u0009', '\n', state7),
				new Transition('\u0020', '\u0020', state7), new Transition('\r', '\r', state7),
				new Transition('\u0031', '\u0031', state1), new Transition('\u0032', '\u0039', state6));
		return Automaton.load(state5, true, null);
	}

	private static Automaton getAut154() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		state0.alter(false, 8, new Transition('\u0030', '\u0030', state1), new Transition('\u0009', '\n', state0),
				new Transition('\u0020', '\u0020', state0), new Transition('\r', '\r', state0),
				new Transition('\u002D', '\u002D', state14), new Transition('\u0031', '\u0039', state2));
		state1.alter(false, 4, new Transition('\u0030', '\u0030', state3), new Transition('\u0031', '\u0039', state4));
		state2.alter(false, 26, new Transition('\u0030', '\u0039', state4));
		state3.alter(false, 23, new Transition('\u0030', '\u0030', state5), new Transition('\u0031', '\u0039', state6));
		state4.alter(false, 17, new Transition('\u0030', '\u0039', state6));
		state5.alter(false, 13, new Transition('\u0030', '\u0030', state6), new Transition('\u0031', '\u0039', state7));
		state6.alter(false, 7, new Transition('\u0030', '\u0039', state7));
		state7.alter(false, 25, new Transition('\u002D', '\u002D', state8), new Transition('\u0030', '\u0039', state7));
		state8.alter(false, 10, new Transition('\u0030', '\u0030', state11),
				new Transition('\u0031', '\u0031', state12));
		state9.alter(false, 14, new Transition('\u0030', '\u0030', state10),
				new Transition('\u0031', '\u0031', state13));
		state10.alter(false, 12, new Transition('\u0030', '\u0039', state26));
		state11.alter(false, 11, new Transition('\u0031', '\u0039', state15));
		state12.alter(false, 27, new Transition('\u0030', '\u0032', state15));
		state13.alter(false, 3, new Transition('\u0034', '\u0034', state16),
				new Transition('\u0030', '\u0033', state26));
		state14.alter(false, 16, new Transition('\u0030', '\u0030', state1),
				new Transition('\u0031', '\u0039', state2));
		state15.alter(false, 2, new Transition('\u002D', '\u002D', state19));
		state16.alter(false, 21, new Transition('\u003A', '\u003A', state20));
		state17.alter(false, 19, new Transition('\u0030', '\u0039', state27));
		state18.alter(false, 22, new Transition('\u0030', '\u0035', state17));
		state19.alter(false, 1, new Transition('\u0030', '\u0030', state23),
				new Transition('\u0031', '\u0032', state22), new Transition('\u0033', '\u0033', state24));
		state20.alter(false, 6, new Transition('\u0030', '\u0030', state25));
		state21.alter(true, 15, new Transition('\u0009', '\n', state27), new Transition('\u0020', '\u0020', state27),
				new Transition('\u002D', '\u002D', state9), new Transition('\r', '\r', state27),
				new Transition('\u005A', '\u005A', state27), new Transition('\u002B', '\u002B', state9));
		state22.alter(false, 20, new Transition('\u0030', '\u0039', state21));
		state23.alter(false, 9, new Transition('\u0031', '\u0039', state21));
		state24.alter(false, 24, new Transition('\u0030', '\u0031', state21));
		state25.alter(false, 0, new Transition('\u0030', '\u0030', state27));
		state26.alter(false, 5, new Transition('\u003A', '\u003A', state18));
		state27.alter(true, 18, new Transition('\u0009', '\n', state27), new Transition('\u0020', '\u0020', state27),
				new Transition('\r', '\r', state27));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut155() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		State state28 = new State();
		State state29 = new State();
		State state30 = new State();
		State state31 = new State();
		State state32 = new State();
		State state33 = new State();
		State state34 = new State();
		State state35 = new State();
		State state36 = new State();
		State state37 = new State();
		State state38 = new State();
		State state39 = new State();
		State state40 = new State();
		State state41 = new State();
		State state42 = new State();
		State state43 = new State();
		State state44 = new State();
		State state45 = new State();
		state0.alter(false, 26, new Transition('\u0009', '\n', state0), new Transition('\u0020', '\u0020', state0),
				new Transition('\u0030', '\u0030', state1), new Transition('\u002D', '\u002D', state16),
				new Transition('\r', '\r', state0), new Transition('\u0031', '\u0039', state2));
		state1.alter(false, 45, new Transition('\u0030', '\u0030', state3), new Transition('\u0031', '\u0039', state4));
		state2.alter(false, 0, new Transition('\u0030', '\u0039', state4));
		state3.alter(false, 18, new Transition('\u0030', '\u0030', state5), new Transition('\u0031', '\u0039', state6));
		state4.alter(false, 19, new Transition('\u0030', '\u0039', state6));
		state5.alter(false, 12, new Transition('\u0030', '\u0030', state6), new Transition('\u0031', '\u0039', state7));
		state6.alter(false, 40, new Transition('\u0030', '\u0039', state7));
		state7.alter(false, 6, new Transition('\u002D', '\u002D', state8), new Transition('\u0030', '\u0039', state7));
		state8.alter(false, 29, new Transition('\u0030', '\u0030', state9),
				new Transition('\u0031', '\u0031', state10));
		state9.alter(false, 14, new Transition('\u0031', '\u0039', state11));
		state10.alter(false, 21, new Transition('\u0030', '\u0032', state11));
		state11.alter(false, 16, new Transition('\u002D', '\u002D', state12));
		state12.alter(false, 9, new Transition('\u0030', '\u0030', state14),
				new Transition('\u0031', '\u0032', state13), new Transition('\u0033', '\u0033', state15));
		state13.alter(false, 24, new Transition('\u0030', '\u0039', state20));
		state14.alter(false, 17, new Transition('\u0031', '\u0039', state20));
		state15.alter(false, 7, new Transition('\u0030', '\u0031', state20));
		state16.alter(false, 8, new Transition('\u0030', '\u0030', state1), new Transition('\u0031', '\u0039', state2));
		state17.alter(false, 37, new Transition('\u0030', '\u0031', state18),
				new Transition('\u0032', '\u0032', state19));
		state18.alter(false, 15, new Transition('\u0030', '\u0039', state44));
		state19.alter(false, 38, new Transition('\u0034', '\u0034', state21),
				new Transition('\u0030', '\u0033', state44));
		state20.alter(false, 43, new Transition('\u0054', '\u0054', state17));
		state21.alter(false, 31, new Transition('\u003A', '\u003A', state25));
		state22.alter(false, 39, new Transition('\u0030', '\u0039', state33));
		state23.alter(false, 10, new Transition('\u0030', '\u0035', state22));
		state24.alter(false, 13, new Transition('\u0030', '\u0030', state28),
				new Transition('\u0031', '\u0031', state29));
		state25.alter(false, 32, new Transition('\u0030', '\u0030', state30));
		state26.alter(false, 44, new Transition('\u003A', '\u003A', state37));
		state27.alter(false, 33, new Transition('\u003A', '\u003A', state38));
		state28.alter(false, 22, new Transition('\u0030', '\u0039', state26));
		state29.alter(false, 4, new Transition('\u0034', '\u0034', state27),
				new Transition('\u0030', '\u0033', state26));
		state30.alter(false, 11, new Transition('\u0030', '\u0030', state34));
		state31.alter(false, 41, new Transition('\u0030', '\u0035', state41));
		state32.alter(false, 20, new Transition('\u0030', '\u0030', state42));
		state33.alter(false, 34, new Transition('\u003A', '\u003A', state31));
		state34.alter(false, 27, new Transition('\u003A', '\u003A', state32));
		state35.alter(false, 5, new Transition('\u0030', '\u0039', state45));
		state36.alter(false, 30, new Transition('\u0030', '\u0030', state45));
		state37.alter(false, 3, new Transition('\u0030', '\u0035', state35));
		state38.alter(false, 36, new Transition('\u0030', '\u0030', state36));
		state39.alter(false, 25, new Transition('\u0030', '\u0039', state43));
		state40.alter(true, 1, new Transition('\u0009', '\n', state45), new Transition('\u0020', '\u0020', state45),
				new Transition('\r', '\r', state45), new Transition('\u002D', '\u002D', state24),
				new Transition('\u005A', '\u005A', state45), new Transition('\u002E', '\u002E', state39),
				new Transition('\u002B', '\u002B', state24));
		state41.alter(false, 28, new Transition('\u0030', '\u0039', state40));
		state42.alter(false, 42, new Transition('\u0030', '\u0030', state40));
		state43.alter(true, 35, new Transition('\u0009', '\n', state45), new Transition('\u0020', '\u0020', state45),
				new Transition('\r', '\r', state45), new Transition('\u002D', '\u002D', state24),
				new Transition('\u005A', '\u005A', state45), new Transition('\u002B', '\u002B', state24),
				new Transition('\u0030', '\u0039', state43));
		state44.alter(false, 23, new Transition('\u003A', '\u003A', state23));
		state45.alter(true, 2, new Transition('\u0009', '\n', state45), new Transition('\u0020', '\u0020', state45),
				new Transition('\r', '\r', state45));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut156() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		state0.alter(false, 4, new Transition('\u0030', '\u0039', state5));
		state1.alter(true, 1, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u002E', '\u002E', state0),
				new Transition('\u0030', '\u0039', state1));
		state2.alter(false, 2, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2), new Transition('\u002D', '\u002D', state4),
				new Transition('\u002B', '\u002B', state4), new Transition('\u0030', '\u0039', state1));
		state3.alter(true, 5, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3));
		state4.alter(false, 3, new Transition('\u0030', '\u0039', state1));
		state5.alter(true, 0, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0030', '\u0039', state5));
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut157() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		state0.alter(false, 3, new Transition('\u002E', '\u002E', state6), new Transition('\u0030', '\u0039', state0),
				new Transition('\u0053', '\u0053', state3));
		state1.alter(false, 8, new Transition('\u0050', '\u0050', state13));
		state2.alter(false, 6, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\u0050', '\u0050', state13), new Transition('\r', '\r', state2),
				new Transition('\u002D', '\u002D', state1));
		state3.alter(true, 15, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3));
		state4.alter(true, 7, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0054', '\u0054', state8),
				new Transition('\u0030', '\u0039', state12));
		state5.alter(true, 5, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0030', '\u0039', state0));
		state6.alter(false, 14, new Transition('\u0030', '\u0039', state11));
		state7.alter(true, 13, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0030', '\u0039', state16));
		state8.alter(false, 1, new Transition('\u0030', '\u0039', state14));
		state9.alter(true, 10, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0054', '\u0054', state8),
				new Transition('\u0030', '\u0039', state15));
		state10.alter(false, 4, new Transition('\u004D', '\u004D', state4), new Transition('\u0044', '\u0044', state17),
				new Transition('\u0030', '\u0039', state10), new Transition('\u0059', '\u0059', state9));
		state11.alter(false, 16, new Transition('\u0030', '\u0039', state11),
				new Transition('\u0053', '\u0053', state3));
		state12.alter(false, 9, new Transition('\u0044', '\u0044', state17),
				new Transition('\u0030', '\u0039', state12));
		state13.alter(false, 12, new Transition('\u0054', '\u0054', state8),
				new Transition('\u0030', '\u0039', state10));
		state14.alter(false, 17, new Transition('\u004D', '\u004D', state5), new Transition('\u002E', '\u002E', state6),
				new Transition('\u0048', '\u0048', state7), new Transition('\u0030', '\u0039', state14),
				new Transition('\u0053', '\u0053', state3));
		state15.alter(false, 2, new Transition('\u004D', '\u004D', state4), new Transition('\u0044', '\u0044', state17),
				new Transition('\u0030', '\u0039', state15));
		state16.alter(false, 11, new Transition('\u004D', '\u004D', state5), new Transition('\u002E', '\u002E', state6),
				new Transition('\u0030', '\u0039', state16), new Transition('\u0053', '\u0053', state3));
		state17.alter(true, 0, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0054', '\u0054', state8));
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut158() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		state0.alter(false, 7, new Transition('\u0030', '\u0039', state5));
		state1.alter(false, 8, new Transition('\u0030', '\u0039', state3), new Transition('\u0049', '\u0049', state10));
		state2.alter(false, 4, new Transition('\u0030', '\u0039', state13));
		state3.alter(true, 9, new Transition('\u0009', '\n', state11), new Transition('\u0020', '\u0020', state11),
				new Transition('\r', '\r', state11), new Transition('\u002E', '\u002E', state0),
				new Transition('\u0045', '\u0045', state12), new Transition('\u0065', '\u0065', state12),
				new Transition('\u0030', '\u0039', state3));
		state4.alter(false, 13, new Transition('\u0061', '\u0061', state8));
		state5.alter(true, 2, new Transition('\u0009', '\n', state11), new Transition('\u0020', '\u0020', state11),
				new Transition('\r', '\r', state11), new Transition('\u0045', '\u0045', state12),
				new Transition('\u0065', '\u0065', state12), new Transition('\u0030', '\u0039', state5));
		state6.alter(false, 1, new Transition('\u0030', '\u0039', state3));
		state7.alter(false, 12, new Transition('\u0009', '\n', state7), new Transition('\u0020', '\u0020', state7),
				new Transition('\u002D', '\u002D', state1), new Transition('\r', '\r', state7),
				new Transition('\u004E', '\u004E', state4), new Transition('\u002B', '\u002B', state6),
				new Transition('\u0030', '\u0039', state3), new Transition('\u0049', '\u0049', state10));
		state8.alter(false, 10, new Transition('\u004E', '\u004E', state11));
		state9.alter(false, 11, new Transition('\u0046', '\u0046', state11));
		state10.alter(false, 0, new Transition('\u004E', '\u004E', state9));
		state11.alter(true, 5, new Transition('\u0009', '\n', state11), new Transition('\u0020', '\u0020', state11),
				new Transition('\r', '\r', state11));
		state12.alter(false, 6, new Transition('\u002D', '\u002D', state2), new Transition('\u002B', '\u002B', state2),
				new Transition('\u0030', '\u0039', state13));
		state13.alter(true, 3, new Transition('\u0009', '\n', state11), new Transition('\u0020', '\u0020', state11),
				new Transition('\r', '\r', state11), new Transition('\u0030', '\u0039', state13));
		return Automaton.load(state7, true, null);
	}

	private static Automaton getAut159() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		state0.alter(false, 13, new Transition('\u0030', '\u0039', state3));
		state1.alter(false, 3, new Transition('\u0030', '\u0030', state0), new Transition('\u0031', '\u0031', state8));
		state2.alter(false, 12, new Transition('\u002D', '\u002D', state7));
		state3.alter(false, 7, new Transition('\u003A', '\u003A', state15));
		state4.alter(false, 14, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\u002D', '\u002D', state2), new Transition('\r', '\r', state4));
		state5.alter(false, 0, new Transition('\u003A', '\u003A', state9));
		state6.alter(false, 9, new Transition('\u0030', '\u0039', state16));
		state7.alter(false, 8, new Transition('\u0030', '\u0030', state13), new Transition('\u0031', '\u0032', state11),
				new Transition('\u0033', '\u0033', state12));
		state8.alter(false, 11, new Transition('\u0034', '\u0034', state5), new Transition('\u0030', '\u0033', state3));
		state9.alter(false, 6, new Transition('\u0030', '\u0030', state14));
		state10.alter(true, 10, new Transition('\u0009', '\n', state16), new Transition('\u0020', '\u0020', state16),
				new Transition('\u002D', '\u002D', state1), new Transition('\r', '\r', state16),
				new Transition('\u005A', '\u005A', state16), new Transition('\u002B', '\u002B', state1));
		state11.alter(false, 5, new Transition('\u0030', '\u0039', state10));
		state12.alter(false, 2, new Transition('\u0030', '\u0031', state10));
		state13.alter(false, 15, new Transition('\u0031', '\u0039', state10));
		state14.alter(false, 1, new Transition('\u0030', '\u0030', state16));
		state15.alter(false, 16, new Transition('\u0030', '\u0035', state6));
		state16.alter(true, 4, new Transition('\u0009', '\n', state16), new Transition('\u0020', '\u0020', state16),
				new Transition('\r', '\r', state16));
		return Automaton.load(state4, true, null);
	}

	private static Automaton getAut160() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		state0.alter(false, 12, new Transition('\u0030', '\u0039', state14));
		state1.alter(false, 1, new Transition('\u0009', '\n', state1), new Transition('\u0020', '\u0020', state1),
				new Transition('\r', '\r', state1), new Transition('\u002D', '\u002D', state4));
		state2.alter(false, 9, new Transition('\u0034', '\u0034', state5), new Transition('\u0030', '\u0033', state14));
		state3.alter(false, 0, new Transition('\u0030', '\u0030', state0), new Transition('\u0031', '\u0031', state2));
		state4.alter(false, 4, new Transition('\u002D', '\u002D', state7));
		state5.alter(false, 14, new Transition('\u003A', '\u003A', state8));
		state6.alter(false, 10, new Transition('\u0030', '\u0035', state10));
		state7.alter(false, 11, new Transition('\u0030', '\u0030', state12),
				new Transition('\u0031', '\u0031', state11));
		state8.alter(false, 7, new Transition('\u0030', '\u0030', state13));
		state9.alter(true, 2, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u002D', '\u002D', state3), new Transition('\r', '\r', state15),
				new Transition('\u005A', '\u005A', state15), new Transition('\u002B', '\u002B', state3));
		state10.alter(false, 3, new Transition('\u0030', '\u0039', state15));
		state11.alter(false, 5, new Transition('\u0030', '\u0032', state9));
		state12.alter(false, 8, new Transition('\u0031', '\u0039', state9));
		state13.alter(false, 6, new Transition('\u0030', '\u0030', state15));
		state14.alter(false, 15, new Transition('\u003A', '\u003A', state6));
		state15.alter(true, 13, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\r', '\r', state15));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut161() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		state0.alter(false, 20, new Transition('\u002D', '\u002D', state7));
		state1.alter(false, 2, new Transition('\u0009', '\n', state1), new Transition('\u0020', '\u0020', state1),
				new Transition('\r', '\r', state1), new Transition('\u002D', '\u002D', state0));
		state2.alter(false, 3, new Transition('\u0030', '\u0030', state3), new Transition('\u0031', '\u0031', state6));
		state3.alter(false, 13, new Transition('\u0030', '\u0039', state19));
		state4.alter(false, 8, new Transition('\u0031', '\u0039', state8));
		state5.alter(false, 7, new Transition('\u0030', '\u0032', state8));
		state6.alter(false, 6, new Transition('\u0034', '\u0034', state9), new Transition('\u0030', '\u0033', state19));
		state7.alter(false, 5, new Transition('\u0030', '\u0030', state4), new Transition('\u0031', '\u0031', state5));
		state8.alter(false, 11, new Transition('\u002D', '\u002D', state12));
		state9.alter(false, 14, new Transition('\u003A', '\u003A', state13));
		state10.alter(false, 1, new Transition('\u0030', '\u0039', state20));
		state11.alter(false, 19, new Transition('\u0030', '\u0035', state10));
		state12.alter(false, 10, new Transition('\u0030', '\u0030', state16),
				new Transition('\u0031', '\u0032', state15), new Transition('\u0033', '\u0033', state17));
		state13.alter(false, 4, new Transition('\u0030', '\u0030', state18));
		state14.alter(true, 17, new Transition('\u0009', '\n', state20), new Transition('\u0020', '\u0020', state20),
				new Transition('\u002D', '\u002D', state2), new Transition('\r', '\r', state20),
				new Transition('\u005A', '\u005A', state20), new Transition('\u002B', '\u002B', state2));
		state15.alter(false, 18, new Transition('\u0030', '\u0039', state14));
		state16.alter(false, 12, new Transition('\u0031', '\u0039', state14));
		state17.alter(false, 0, new Transition('\u0030', '\u0031', state14));
		state18.alter(false, 15, new Transition('\u0030', '\u0030', state20));
		state19.alter(false, 16, new Transition('\u003A', '\u003A', state11));
		state20.alter(true, 9, new Transition('\u0009', '\n', state20), new Transition('\u0020', '\u0020', state20),
				new Transition('\r', '\r', state20));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut162() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		state0.alter(false, 9, new Transition('\u0030', '\u0039', state17));
		state1.alter(false, 11, new Transition('\u0030', '\u0030', state6), new Transition('\u0031', '\u0039', state5));
		state2.alter(false, 10, new Transition('\u0030', '\u0030', state6), new Transition('\u0009', '\n', state2),
				new Transition('\u0020', '\u0020', state2), new Transition('\r', '\r', state2),
				new Transition('\u002D', '\u002D', state1), new Transition('\u0031', '\u0039', state5));
		state3.alter(false, 3, new Transition('\u0034', '\u0034', state7), new Transition('\u0030', '\u0033', state17));
		state4.alter(false, 0, new Transition('\u0030', '\u0030', state0), new Transition('\u0031', '\u0031', state3));
		state5.alter(false, 16, new Transition('\u0030', '\u0039', state11));
		state6.alter(false, 4, new Transition('\u0030', '\u0030', state12),
				new Transition('\u0031', '\u0039', state11));
		state7.alter(false, 17, new Transition('\u003A', '\u003A', state13));
		state8.alter(false, 15, new Transition('\u0030', '\u0035', state14));
		state9.alter(false, 12, new Transition('\u0030', '\u0030', state16),
				new Transition('\u0031', '\u0039', state15));
		state10.alter(false, 5, new Transition('\u0030', '\u0030', state18));
		state11.alter(false, 2, new Transition('\u0030', '\u0039', state16));
		state12.alter(false, 6, new Transition('\u0030', '\u0030', state9),
				new Transition('\u0031', '\u0039', state16));
		state13.alter(false, 1, new Transition('\u0030', '\u0030', state10));
		state14.alter(false, 14, new Transition('\u0030', '\u0039', state18));
		state15.alter(true, 18, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\u002D', '\u002D', state4), new Transition('\r', '\r', state18),
				new Transition('\u005A', '\u005A', state18), new Transition('\u002B', '\u002B', state4),
				new Transition('\u0030', '\u0039', state15));
		state16.alter(false, 13, new Transition('\u0030', '\u0039', state15));
		state17.alter(false, 7, new Transition('\u003A', '\u003A', state8));
		state18.alter(true, 8, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18));
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut163() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		state0.alter(false, 21, new Transition('\u0030', '\u0030', state1), new Transition('\u0009', '\n', state0),
				new Transition('\u0020', '\u0020', state0), new Transition('\u002D', '\u002D', state10),
				new Transition('\r', '\r', state0), new Transition('\u0031', '\u0039', state2));
		state1.alter(false, 14, new Transition('\u0030', '\u0030', state3), new Transition('\u0031', '\u0039', state4));
		state2.alter(false, 9, new Transition('\u0030', '\u0039', state4));
		state3.alter(false, 17, new Transition('\u0030', '\u0030', state7), new Transition('\u0031', '\u0039', state8));
		state4.alter(false, 3, new Transition('\u0030', '\u0039', state8));
		state5.alter(false, 8, new Transition('\u0030', '\u0030', state6), new Transition('\u0031', '\u0031', state9));
		state6.alter(false, 12, new Transition('\u0030', '\u0039', state21));
		state7.alter(false, 11, new Transition('\u0030', '\u0030', state8),
				new Transition('\u0031', '\u0039', state11));
		state8.alter(false, 0, new Transition('\u0030', '\u0039', state11));
		state9.alter(false, 16, new Transition('\u0034', '\u0034', state12),
				new Transition('\u0030', '\u0033', state21));
		state10.alter(false, 6, new Transition('\u0030', '\u0030', state1), new Transition('\u0031', '\u0039', state2));
		state11.alter(false, 19, new Transition('\u002D', '\u002D', state14),
				new Transition('\u0030', '\u0039', state11));
		state12.alter(false, 18, new Transition('\u003A', '\u003A', state15));
		state13.alter(false, 15, new Transition('\u0030', '\u0035', state17));
		state14.alter(false, 7, new Transition('\u0030', '\u0030', state18),
				new Transition('\u0031', '\u0031', state19));
		state15.alter(false, 22, new Transition('\u0030', '\u0030', state20));
		state16.alter(true, 20, new Transition('\u0009', '\n', state22), new Transition('\u0020', '\u0020', state22),
				new Transition('\r', '\r', state22), new Transition('\u002D', '\u002D', state5),
				new Transition('\u005A', '\u005A', state22), new Transition('\u002B', '\u002B', state5));
		state17.alter(false, 13, new Transition('\u0030', '\u0039', state22));
		state18.alter(false, 1, new Transition('\u0031', '\u0039', state16));
		state19.alter(false, 4, new Transition('\u0030', '\u0032', state16));
		state20.alter(false, 5, new Transition('\u0030', '\u0030', state22));
		state21.alter(false, 10, new Transition('\u003A', '\u003A', state13));
		state22.alter(true, 2, new Transition('\u0009', '\n', state22), new Transition('\u0020', '\u0020', state22),
				new Transition('\r', '\r', state22));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut164() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(true, 2, new Transition('\u0009', '\n', state0), new Transition('\u0020', '\u0020', state0),
				new Transition('\r', '\r', state0), new Transition('\u0041', '\u0046', state2),
				new Transition('\u0061', '\u0066', state2), new Transition('\u0030', '\u0039', state2));
		state1.alter(true, 1, new Transition('\u0009', '\n', state1), new Transition('\u0020', '\u0020', state1),
				new Transition('\r', '\r', state1));
		state2.alter(false, 0, new Transition('\u0041', '\u0046', state3), new Transition('\u0061', '\u0066', state3),
				new Transition('\u0030', '\u0039', state3));
		state3.alter(true, 3, new Transition('\u0009', '\n', state1), new Transition('\u0020', '\u0020', state1),
				new Transition('\r', '\r', state1), new Transition('\u0041', '\u0046', state2),
				new Transition('\u0061', '\u0066', state2), new Transition('\u0030', '\u0039', state2));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut165() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		State state28 = new State();
		State state29 = new State();
		State state30 = new State();
		State state31 = new State();
		state0.alter(true, 22, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state3));
		state1.alter(true, 24, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\u0030', '\u0030', state1), new Transition('\r', '\r', state29),
				new Transition('\u0033', '\u0039', state3), new Transition('\u0031', '\u0031', state0),
				new Transition('\u0032', '\u0032', state4));
		state2.alter(true, 2, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\u0030', '\u0030', state3), new Transition('\r', '\r', state29),
				new Transition('\u0031', '\u0031', state5), new Transition('\u0032', '\u0039', state6));
		state3.alter(true, 19, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state6));
		state4.alter(true, 7, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\u0030', '\u0030', state3), new Transition('\r', '\r', state29),
				new Transition('\u0031', '\u0031', state7), new Transition('\u0032', '\u0039', state6));
		state5.alter(true, 26, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0034', state8),
				new Transition('\u0035', '\u0039', state9), new Transition('\u0030', '\u0033', state6));
		state6.alter(true, 28, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state9));
		state7.alter(true, 31, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0034', state10),
				new Transition('\u0035', '\u0039', state9), new Transition('\u0030', '\u0033', state6));
		state8.alter(true, 16, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0036', state9),
				new Transition('\u0037', '\u0037', state11), new Transition('\u0038', '\u0039', state12));
		state9.alter(true, 29, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state12));
		state10.alter(true, 20, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0036', state9),
				new Transition('\u0037', '\u0037', state13), new Transition('\u0038', '\u0039', state12));
		state11.alter(true, 18, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0034', state14),
				new Transition('\u0035', '\u0039', state15), new Transition('\u0030', '\u0033', state12));
		state12.alter(true, 5, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state15));
		state13.alter(true, 8, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0034', state16),
				new Transition('\u0035', '\u0039', state15), new Transition('\u0030', '\u0033', state12));
		state14.alter(true, 14, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0037', state15),
				new Transition('\u0038', '\u0038', state17), new Transition('\u0039', '\u0039', state18));
		state15.alter(true, 4, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state18));
		state16.alter(true, 15, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0037', state15),
				new Transition('\u0038', '\u0038', state19), new Transition('\u0039', '\u0039', state18));
		state17.alter(true, 27, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0039', state21),
				new Transition('\u0030', '\u0032', state18), new Transition('\u0033', '\u0033', state20));
		state18.alter(true, 3, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state21));
		state19.alter(true, 25, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0039', state21),
				new Transition('\u0030', '\u0032', state18), new Transition('\u0033', '\u0033', state22));
		state20.alter(true, 6, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0037', '\u0039', state25),
				new Transition('\u0036', '\u0036', state23), new Transition('\u0030', '\u0035', state21));
		state21.alter(true, 0, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state25));
		state22.alter(true, 10, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0037', '\u0039', state25),
				new Transition('\u0036', '\u0036', state27), new Transition('\u0030', '\u0035', state21));
		state23.alter(true, 21, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0034', state28),
				new Transition('\u0035', '\u0039', state29), new Transition('\u0030', '\u0033', state25));
		state24.alter(false, 30, new Transition('\u0030', '\u0030', state31), new Transition('\u0009', '\n', state24),
				new Transition('\u0020', '\u0020', state24), new Transition('\u0033', '\u0039', state3),
				new Transition('\u002D', '\u002D', state30), new Transition('\r', '\r', state24),
				new Transition('\u0031', '\u0031', state0), new Transition('\u0032', '\u0032', state2));
		state25.alter(true, 17, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0039', state29));
		state26.alter(true, 11, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0038', state29));
		state27.alter(true, 1, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0034', '\u0034', state26),
				new Transition('\u0035', '\u0039', state29), new Transition('\u0030', '\u0033', state25));
		state28.alter(true, 12, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29), new Transition('\u0030', '\u0037', state29));
		state29.alter(true, 13, new Transition('\u0009', '\n', state29), new Transition('\u0020', '\u0020', state29),
				new Transition('\r', '\r', state29));
		state30.alter(false, 23, new Transition('\u0030', '\u0030', state1), new Transition('\u0033', '\u0039', state3),
				new Transition('\u0031', '\u0031', state0), new Transition('\u0032', '\u0032', state4));
		state31.alter(true, 9, new Transition('\u0030', '\u0030', state31), new Transition('\u0009', '\n', state29),
				new Transition('\u0020', '\u0020', state29), new Transition('\r', '\r', state29),
				new Transition('\u0033', '\u0039', state3), new Transition('\u0031', '\u0031', state0),
				new Transition('\u0032', '\u0032', state2));
		return Automaton.load(state24, true, null);
	}

	private static Automaton getAut166() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 0, new Transition('\u0030', '\u0039', state1));
		state1.alter(true, 3, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0030', '\u0039', state1));
		state2.alter(false, 2, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\u002D', '\u002D', state0), new Transition('\r', '\r', state2),
				new Transition('\u002B', '\u002B', state0), new Transition('\u0030', '\u0039', state1));
		state3.alter(true, 1, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3));
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut167() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		state0.alter(true, 2, new Transition('\u0041', '\u005A', state1), new Transition('\u0061', '\u007A', state1),
				new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state1.alter(true, 3, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state2), new Transition('\u0061', '\u007A', state2),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state2.alter(true, 9, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state7), new Transition('\u0061', '\u007A', state7),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state3.alter(true, 6, new Transition('\u0041', '\u005A', state5), new Transition('\u0061', '\u007A', state5),
				new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state5));
		state4.alter(true, 11, new Transition('\u0041', '\u005A', state0), new Transition('\u0061', '\u007A', state0),
				new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state5.alter(true, 0, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state12), new Transition('\u0061', '\u007A', state12),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state12));
		state6.alter(true, 17, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state10), new Transition('\u0061', '\u007A', state10),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state7.alter(true, 15, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state6), new Transition('\u0061', '\u007A', state6),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state8.alter(true, 16, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state3), new Transition('\u0061', '\u007A', state3),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state3));
		state9.alter(true, 12, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state10.alter(true, 13, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state9), new Transition('\u0061', '\u007A', state9),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15));
		state11.alter(true, 14, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state17), new Transition('\u0061', '\u007A', state17),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state17));
		state12.alter(true, 10, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state11), new Transition('\u0061', '\u007A', state11),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state11));
		state13.alter(true, 1, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u0041', '\u005A', state9), new Transition('\u0061', '\u007A', state9),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state9));
		state14.alter(false, 8, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\u0041', '\u005A', state4), new Transition('\u0061', '\u007A', state4),
				new Transition('\r', '\r', state14));
		state15.alter(true, 5, new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\r', '\r', state15));
		state16.alter(false, 4, new Transition('\u0041', '\u005A', state8), new Transition('\u0061', '\u007A', state8),
				new Transition('\u0030', '\u0039', state8));
		state17.alter(true, 7, new Transition('\u0041', '\u005A', state13), new Transition('\u0061', '\u007A', state13),
				new Transition('\u0009', '\n', state15), new Transition('\u0020', '\u0020', state15),
				new Transition('\u002D', '\u002D', state16), new Transition('\r', '\r', state15),
				new Transition('\u0030', '\u0039', state13));
		return Automaton.load(state14, true, null);
	}

	private static Automaton getAut168() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		State state28 = new State();
		State state29 = new State();
		State state30 = new State();
		State state31 = new State();
		State state32 = new State();
		State state33 = new State();
		State state34 = new State();
		State state35 = new State();
		State state36 = new State();
		State state37 = new State();
		State state38 = new State();
		State state39 = new State();
		State state40 = new State();
		State state41 = new State();
		State state42 = new State();
		State state43 = new State();
		State state44 = new State();
		State state45 = new State();
		State state46 = new State();
		State state47 = new State();
		State state48 = new State();
		State state49 = new State();
		State state50 = new State();
		State state51 = new State();
		State state52 = new State();
		State state53 = new State();
		State state54 = new State();
		State state55 = new State();
		State state56 = new State();
		State state57 = new State();
		State state58 = new State();
		state0.alter(true, 17, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state3));
		state1.alter(true, 53, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\u0030', '\u0030', state1), new Transition('\r', '\r', state56),
				new Transition('\u0031', '\u0038', state0), new Transition('\u0039', '\u0039', state4));
		state2.alter(true, 6, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0033', '\u0039', state6),
				new Transition('\u0030', '\u0031', state3), new Transition('\u0032', '\u0032', state5));
		state3.alter(true, 16, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state6));
		state4.alter(true, 12, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0033', '\u0039', state6),
				new Transition('\u0030', '\u0031', state3), new Transition('\u0032', '\u0032', state7));
		state5.alter(true, 57, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\u0033', '\u0039', state9), new Transition('\r', '\r', state56),
				new Transition('\u0030', '\u0031', state6), new Transition('\u0032', '\u0032', state8));
		state6.alter(true, 54, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state9));
		state7.alter(true, 58, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\u0033', '\u0039', state9), new Transition('\r', '\r', state56),
				new Transition('\u0030', '\u0031', state6), new Transition('\u0032', '\u0032', state10));
		state8.alter(true, 42, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0039', state12),
				new Transition('\u0030', '\u0032', state9), new Transition('\u0033', '\u0033', state11));
		state9.alter(true, 19, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state12));
		state10.alter(true, 46, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0039', state12),
				new Transition('\u0030', '\u0032', state9), new Transition('\u0033', '\u0033', state13));
		state11.alter(true, 9, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0039', state15),
				new Transition('\u0030', '\u0032', state12), new Transition('\u0033', '\u0033', state14));
		state12.alter(true, 30, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state15));
		state13.alter(true, 11, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0039', state15),
				new Transition('\u0030', '\u0032', state12), new Transition('\u0033', '\u0033', state16));
		state14.alter(true, 31, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0036', state15),
				new Transition('\u0037', '\u0037', state17), new Transition('\u0038', '\u0039', state18));
		state15.alter(true, 4, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state18));
		state16.alter(true, 10, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0036', state15),
				new Transition('\u0037', '\u0037', state19), new Transition('\u0038', '\u0039', state18));
		state17.alter(true, 55, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\u0033', '\u0039', state21), new Transition('\r', '\r', state56),
				new Transition('\u0030', '\u0031', state18), new Transition('\u0032', '\u0032', state20));
		state18.alter(true, 15, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state21));
		state19.alter(true, 29, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\u0033', '\u0039', state21), new Transition('\r', '\r', state56),
				new Transition('\u0030', '\u0031', state18), new Transition('\u0032', '\u0032', state22));
		state20.alter(true, 45, new Transition('\u0030', '\u0030', state23), new Transition('\u0009', '\n', state56),
				new Transition('\u0020', '\u0020', state56), new Transition('\r', '\r', state56),
				new Transition('\u0031', '\u0039', state24));
		state21.alter(true, 36, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state24));
		state22.alter(true, 26, new Transition('\u0030', '\u0030', state25), new Transition('\u0009', '\n', state56),
				new Transition('\u0020', '\u0020', state56), new Transition('\r', '\r', state56),
				new Transition('\u0031', '\u0039', state24));
		state23.alter(true, 24, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0039', state27),
				new Transition('\u0030', '\u0032', state24), new Transition('\u0033', '\u0033', state26));
		state24.alter(true, 13, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state27));
		state25.alter(true, 35, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0039', state27),
				new Transition('\u0030', '\u0032', state24), new Transition('\u0033', '\u0033', state28));
		state26.alter(true, 21, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0037', '\u0039', state30),
				new Transition('\u0036', '\u0036', state29), new Transition('\u0030', '\u0035', state27));
		state27.alter(true, 38, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state30));
		state28.alter(true, 20, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0037', '\u0039', state30),
				new Transition('\u0036', '\u0036', state31), new Transition('\u0030', '\u0035', state27));
		state29.alter(true, 40, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0037', state30),
				new Transition('\u0038', '\u0038', state32), new Transition('\u0039', '\u0039', state33));
		state30.alter(true, 49, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state33));
		state31.alter(true, 52, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0037', state30),
				new Transition('\u0038', '\u0038', state34), new Transition('\u0039', '\u0039', state33));
		state32.alter(true, 27, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0036', '\u0039', state36),
				new Transition('\u0035', '\u0035', state35), new Transition('\u0030', '\u0034', state33));
		state33.alter(true, 37, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state36));
		state34.alter(true, 7, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0036', '\u0039', state36),
				new Transition('\u0035', '\u0035', state37), new Transition('\u0030', '\u0034', state33));
		state35.alter(true, 5, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0034', state38),
				new Transition('\u0035', '\u0039', state39), new Transition('\u0030', '\u0033', state36));
		state36.alter(true, 56, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state39));
		state37.alter(true, 0, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0034', '\u0034', state40),
				new Transition('\u0035', '\u0039', state39), new Transition('\u0030', '\u0033', state36));
		state38.alter(true, 28, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0036', state39),
				new Transition('\u0037', '\u0037', state41), new Transition('\u0038', '\u0039', state42));
		state39.alter(true, 25, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state42));
		state40.alter(true, 22, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0036', state39),
				new Transition('\u0037', '\u0037', state43), new Transition('\u0038', '\u0039', state42));
		state41.alter(true, 32, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0036', state42),
				new Transition('\u0037', '\u0037', state44), new Transition('\u0038', '\u0039', state45));
		state42.alter(true, 47, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state45));
		state43.alter(true, 41, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0036', state42),
				new Transition('\u0037', '\u0037', state46), new Transition('\u0038', '\u0039', state45));
		state44.alter(true, 2, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0036', '\u0039', state48),
				new Transition('\u0035', '\u0035', state47), new Transition('\u0030', '\u0034', state45));
		state45.alter(true, 39, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state48));
		state46.alter(true, 33, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0036', '\u0039', state48),
				new Transition('\u0035', '\u0035', state49), new Transition('\u0030', '\u0034', state45));
		state47.alter(true, 18, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0037', state48),
				new Transition('\u0038', '\u0038', state50), new Transition('\u0039', '\u0039', state52));
		state48.alter(true, 50, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state52));
		state49.alter(true, 23, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0037', state48),
				new Transition('\u0038', '\u0038', state54), new Transition('\u0039', '\u0039', state52));
		state50.alter(true, 3, new Transition('\u0030', '\u0030', state55), new Transition('\u0009', '\n', state56),
				new Transition('\u0020', '\u0020', state56), new Transition('\r', '\r', state56),
				new Transition('\u0031', '\u0039', state56));
		state51.alter(false, 44, new Transition('\u0030', '\u0030', state58), new Transition('\u0009', '\n', state51),
				new Transition('\u0020', '\u0020', state51), new Transition('\r', '\r', state51),
				new Transition('\u002D', '\u002D', state57), new Transition('\u0031', '\u0038', state0),
				new Transition('\u0039', '\u0039', state2));
		state52.alter(true, 51, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0039', state56));
		state53.alter(true, 34, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0038', state56));
		state54.alter(true, 8, new Transition('\u0030', '\u0030', state53), new Transition('\u0009', '\n', state56),
				new Transition('\u0020', '\u0020', state56), new Transition('\r', '\r', state56),
				new Transition('\u0031', '\u0039', state56));
		state55.alter(true, 1, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56), new Transition('\u0030', '\u0037', state56));
		state56.alter(true, 43, new Transition('\u0009', '\n', state56), new Transition('\u0020', '\u0020', state56),
				new Transition('\r', '\r', state56));
		state57.alter(false, 48, new Transition('\u0030', '\u0030', state1), new Transition('\u0031', '\u0038', state0),
				new Transition('\u0039', '\u0039', state4));
		state58.alter(true, 14, new Transition('\u0030', '\u0030', state58), new Transition('\u0009', '\n', state56),
				new Transition('\u0020', '\u0020', state56), new Transition('\r', '\r', state56),
				new Transition('\u0031', '\u0038', state0), new Transition('\u0039', '\u0039', state2));
		return Automaton.load(state51, true, null);
	}

	private static Automaton getAut169() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		state0.alter(false, 1, new Transition('\u0009', '\n', state0), new Transition('\u0020', '\u0020', state0),
				new Transition('\r', '\r', state0), new Transition('\u002D', '\u002D', state2));
		state1.alter(true, 0, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3), new Transition('\u0030', '\u0039', state1));
		state2.alter(false, 2, new Transition('\u0031', '\u0039', state1));
		state3.alter(true, 3, new Transition('\u0009', '\n', state3), new Transition('\u0020', '\u0020', state3),
				new Transition('\r', '\r', state3));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut170() {
		State state0 = new State();
		state0.alter(true, 0, new Transition('\u0026', '\u003F', state0), new Transition('\u0000', '\u0024', state0),
				new Transition('\u0041', '\uFFFF', state0));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut171() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(false, 2, new Transition('\u0009', '\n', state0), new Transition('\u0020', '\u0020', state0),
				new Transition('\r', '\r', state0), new Transition('\u0030', '\u0039', state1));
		state1.alter(true, 1, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2), new Transition('\u0030', '\u0039', state1));
		state2.alter(true, 0, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut172() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		state0.alter(true, 0, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2), new Transition('\u0030', '\u0039', state0));
		state1.alter(false, 3, new Transition('\u0030', '\u0039', state0));
		state2.alter(true, 1, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		state3.alter(false, 4, new Transition('\u0030', '\u0030', state4), new Transition('\u0009', '\n', state3),
				new Transition('\u0020', '\u0020', state3), new Transition('\u002D', '\u002D', state1),
				new Transition('\r', '\r', state3));
		state4.alter(true, 2, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\u0030', '\u0030', state4), new Transition('\r', '\r', state2));
		return Automaton.load(state3, true, null);
	}

	private static Automaton getAut173() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		state0.alter(true, 2, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2), new Transition('\u0030', '\u0039', state0));
		state1.alter(false, 1, new Transition('\u0009', '\n', state1), new Transition('\u0020', '\u0020', state1),
				new Transition('\r', '\r', state1), new Transition('\u0031', '\u0039', state0));
		state2.alter(true, 0, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut174() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		state0.alter(true, 3, new Transition('\u0030', '\u0030', state0), new Transition('\u0009', '\n', state14),
				new Transition('\u0020', '\u0020', state14), new Transition('\r', '\r', state14),
				new Transition('\u0034', '\u0039', state4), new Transition('\u0031', '\u0032', state8),
				new Transition('\u0033', '\u0033', state7));
		state1.alter(true, 5, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\u0033', '\u0039', state16), new Transition('\r', '\r', state14),
				new Transition('\u0030', '\u0031', state4), new Transition('\u0032', '\u0032', state5));
		state2.alter(true, 7, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0036', state16),
				new Transition('\u0037', '\u0037', state12), new Transition('\u0038', '\u0039', state10));
		state3.alter(true, 4, new Transition('\u0030', '\u0030', state3), new Transition('\u0009', '\n', state14),
				new Transition('\u0020', '\u0020', state14), new Transition('\r', '\r', state14),
				new Transition('\u0034', '\u0039', state4), new Transition('\u0031', '\u0032', state8),
				new Transition('\u0033', '\u0033', state1));
		state4.alter(true, 15, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0039', state16));
		state5.alter(true, 2, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0036', state16),
				new Transition('\u0037', '\u0037', state6), new Transition('\u0038', '\u0039', state10));
		state6.alter(true, 6, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0037', '\u0039', state14),
				new Transition('\u0036', '\u0036', state13), new Transition('\u0030', '\u0035', state10));
		state7.alter(true, 10, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\u0033', '\u0039', state16), new Transition('\r', '\r', state14),
				new Transition('\u0030', '\u0031', state4), new Transition('\u0032', '\u0032', state2));
		state8.alter(true, 16, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0039', state4));
		state9.alter(false, 12, new Transition('\u0030', '\u0030', state3), new Transition('\u0009', '\n', state9),
				new Transition('\u0020', '\u0020', state9), new Transition('\r', '\r', state9),
				new Transition('\u002D', '\u002D', state15), new Transition('\u0034', '\u0039', state4),
				new Transition('\u0031', '\u0032', state8), new Transition('\u0033', '\u0033', state1));
		state10.alter(true, 14, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0039', state14));
		state11.alter(true, 1, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0038', state14));
		state12.alter(true, 0, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0037', '\u0039', state14),
				new Transition('\u0036', '\u0036', state11), new Transition('\u0030', '\u0035', state10));
		state13.alter(true, 9, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0037', state14));
		state14.alter(true, 11, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14));
		state15.alter(false, 13, new Transition('\u0030', '\u0030', state0), new Transition('\u0034', '\u0039', state4),
				new Transition('\u0031', '\u0032', state8), new Transition('\u0033', '\u0033', state7));
		state16.alter(true, 8, new Transition('\u0009', '\n', state14), new Transition('\u0020', '\u0020', state14),
				new Transition('\r', '\r', state14), new Transition('\u0030', '\u0039', state10));
		return Automaton.load(state9, true, null);
	}

	private static Automaton getAut175() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 0, new Transition('\uDC00', '\uDFFF', state1));
		state1.alter(true, 1, new Transition('\u0009', '\n', state1), new Transition('\r', '\r', state1),
				new Transition('\uE000', '\uFFFD', state1), new Transition('\uD800', '\uDBFF', state0),
				new Transition('\u0020', '\uD7FF', state1));
		return Automaton.load(state1, true, null);
	}

	private static Automaton getAut176() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		state0.alter(false, 22, new Transition('\u0030', '\u0039', state26));
		state1.alter(false, 7, new Transition('\u0034', '\u0034', state3), new Transition('\u0030', '\u0033', state26));
		state2.alter(false, 14, new Transition('\u0009', '\n', state2), new Transition('\u0020', '\u0020', state2),
				new Transition('\r', '\r', state2), new Transition('\u0030', '\u0031', state0),
				new Transition('\u0032', '\u0032', state1));
		state3.alter(false, 3, new Transition('\u003A', '\u003A', state7));
		state4.alter(false, 5, new Transition('\u0030', '\u0039', state15));
		state5.alter(false, 2, new Transition('\u0030', '\u0035', state4));
		state6.alter(false, 9, new Transition('\u0030', '\u0030', state10),
				new Transition('\u0031', '\u0031', state11));
		state7.alter(false, 18, new Transition('\u0030', '\u0030', state12));
		state8.alter(false, 21, new Transition('\u003A', '\u003A', state19));
		state9.alter(false, 13, new Transition('\u003A', '\u003A', state21));
		state10.alter(false, 16, new Transition('\u0030', '\u0039', state8));
		state11.alter(false, 4, new Transition('\u0034', '\u0034', state9), new Transition('\u0030', '\u0033', state8));
		state12.alter(false, 12, new Transition('\u0030', '\u0030', state16));
		state13.alter(false, 10, new Transition('\u0030', '\u0035', state23));
		state14.alter(false, 26, new Transition('\u0030', '\u0030', state25));
		state15.alter(false, 8, new Transition('\u003A', '\u003A', state13));
		state16.alter(false, 25, new Transition('\u003A', '\u003A', state14));
		state17.alter(false, 24, new Transition('\u0030', '\u0039', state27));
		state18.alter(false, 11, new Transition('\u0030', '\u0030', state27));
		state19.alter(false, 0, new Transition('\u0030', '\u0035', state17));
		state20.alter(false, 27, new Transition('\u0030', '\u0039', state24));
		state21.alter(false, 6, new Transition('\u0030', '\u0030', state18));
		state22.alter(true, 19, new Transition('\u0009', '\n', state27), new Transition('\u0020', '\u0020', state27),
				new Transition('\r', '\r', state27), new Transition('\u002D', '\u002D', state6),
				new Transition('\u005A', '\u005A', state27), new Transition('\u002E', '\u002E', state20),
				new Transition('\u002B', '\u002B', state6));
		state23.alter(false, 15, new Transition('\u0030', '\u0039', state22));
		state24.alter(true, 17, new Transition('\u0009', '\n', state27), new Transition('\u0020', '\u0020', state27),
				new Transition('\r', '\r', state27), new Transition('\u002D', '\u002D', state6),
				new Transition('\u005A', '\u005A', state27), new Transition('\u002B', '\u002B', state6),
				new Transition('\u0030', '\u0039', state24));
		state25.alter(false, 20, new Transition('\u0030', '\u0030', state22));
		state26.alter(false, 23, new Transition('\u003A', '\u003A', state5));
		state27.alter(true, 1, new Transition('\u0009', '\n', state27), new Transition('\u0020', '\u0020', state27),
				new Transition('\r', '\r', state27));
		return Automaton.load(state2, true, null);
	}

	private static Automaton getAut177() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		state0.alter(true, 4, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\r', '\r', state4), new Transition('\u0030', '\u0039', state3));
		state1.alter(true, 5, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\u0030', '\u0030', state1), new Transition('\r', '\r', state4),
				new Transition('\u0033', '\u0039', state3), new Transition('\u0031', '\u0031', state0),
				new Transition('\u0032', '\u0032', state6));
		state2.alter(true, 2, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\r', '\r', state4), new Transition('\u0030', '\u0035', state4));
		state3.alter(true, 6, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\r', '\r', state4), new Transition('\u0030', '\u0039', state4));
		state4.alter(true, 1, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\r', '\r', state4));
		state5.alter(false, 0, new Transition('\u0009', '\n', state5), new Transition('\u0020', '\u0020', state5),
				new Transition('\u0030', '\u0030', state1), new Transition('\r', '\r', state5),
				new Transition('\u0033', '\u0039', state3), new Transition('\u0031', '\u0031', state0),
				new Transition('\u0032', '\u0032', state6));
		state6.alter(true, 3, new Transition('\u0009', '\n', state4), new Transition('\u0020', '\u0020', state4),
				new Transition('\r', '\r', state4), new Transition('\u0036', '\u0039', state4),
				new Transition('\u0035', '\u0035', state2), new Transition('\u0030', '\u0034', state3));
		return Automaton.load(state5, true, null);
	}

	private static Automaton getAut178() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		state0.alter(true, 6, new Transition('\u0030', '\u0030', state0), new Transition('\u0009', '\n', state18),
				new Transition('\u0020', '\u0020', state18), new Transition('\r', '\r', state18),
				new Transition('\u0034', '\u0034', state1), new Transition('\u0035', '\u0039', state2),
				new Transition('\u0031', '\u0033', state20));
		state1.alter(true, 3, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\u0033', '\u0039', state4), new Transition('\r', '\r', state18),
				new Transition('\u0030', '\u0031', state2), new Transition('\u0032', '\u0032', state3));
		state2.alter(true, 19, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state4));
		state3.alter(true, 13, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0038', state4),
				new Transition('\u0039', '\u0039', state5));
		state4.alter(true, 1, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state6));
		state5.alter(true, 14, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0034', '\u0034', state7),
				new Transition('\u0035', '\u0039', state8), new Transition('\u0030', '\u0033', state6));
		state6.alter(true, 10, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state8));
		state7.alter(true, 20, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0038', state8),
				new Transition('\u0039', '\u0039', state9));
		state8.alter(true, 2, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state10));
		state9.alter(true, 11, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0037', '\u0039', state12),
				new Transition('\u0036', '\u0036', state11), new Transition('\u0030', '\u0035', state10));
		state10.alter(true, 16, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state12));
		state11.alter(true, 0, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0036', state12),
				new Transition('\u0037', '\u0037', state13), new Transition('\u0038', '\u0039', state14));
		state12.alter(true, 7, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state14));
		state13.alter(true, 9, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0033', '\u0039', state16),
				new Transition('\u0030', '\u0031', state14), new Transition('\u0032', '\u0032', state15));
		state14.alter(true, 17, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state16));
		state15.alter(true, 18, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0038', state16),
				new Transition('\u0039', '\u0039', state17));
		state16.alter(true, 15, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state18));
		state17.alter(true, 4, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0035', state18));
		state18.alter(true, 12, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18));
		state19.alter(false, 5, new Transition('\u0009', '\n', state19), new Transition('\u0020', '\u0020', state19),
				new Transition('\u0030', '\u0030', state0), new Transition('\r', '\r', state19),
				new Transition('\u0034', '\u0034', state1), new Transition('\u0035', '\u0039', state2),
				new Transition('\u0031', '\u0033', state20));
		state20.alter(true, 8, new Transition('\u0009', '\n', state18), new Transition('\u0020', '\u0020', state18),
				new Transition('\r', '\r', state18), new Transition('\u0030', '\u0039', state2));
		return Automaton.load(state19, true, null);
	}

	private static Automaton getAut179() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		State state11 = new State();
		State state12 = new State();
		State state13 = new State();
		State state14 = new State();
		State state15 = new State();
		State state16 = new State();
		State state17 = new State();
		State state18 = new State();
		State state19 = new State();
		State state20 = new State();
		State state21 = new State();
		State state22 = new State();
		State state23 = new State();
		State state24 = new State();
		State state25 = new State();
		State state26 = new State();
		State state27 = new State();
		State state28 = new State();
		State state29 = new State();
		State state30 = new State();
		State state31 = new State();
		State state32 = new State();
		State state33 = new State();
		State state34 = new State();
		State state35 = new State();
		State state36 = new State();
		State state37 = new State();
		State state38 = new State();
		State state39 = new State();
		state0.alter(true, 12, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state2));
		state1.alter(true, 16, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0037', state0),
				new Transition('\u0038', '\u0038', state3), new Transition('\u0039', '\u0039', state2));
		state2.alter(true, 11, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state4));
		state3.alter(true, 0, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0034', '\u0034', state5),
				new Transition('\u0035', '\u0039', state4), new Transition('\u0030', '\u0033', state2));
		state4.alter(true, 32, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state6));
		state5.alter(true, 20, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0034', '\u0034', state7),
				new Transition('\u0035', '\u0039', state6), new Transition('\u0030', '\u0033', state4));
		state6.alter(true, 15, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state8));
		state7.alter(true, 8, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0037', '\u0039', state8),
				new Transition('\u0036', '\u0036', state9), new Transition('\u0030', '\u0035', state6));
		state8.alter(true, 26, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state10));
		state9.alter(true, 27, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0036', state8),
				new Transition('\u0037', '\u0037', state11), new Transition('\u0038', '\u0039', state10));
		state10.alter(true, 29, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state12));
		state11.alter(true, 4, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0034', '\u0034', state13),
				new Transition('\u0035', '\u0039', state12), new Transition('\u0030', '\u0033', state10));
		state12.alter(true, 23, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state14));
		state13.alter(true, 33, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0034', '\u0034', state15),
				new Transition('\u0035', '\u0039', state14), new Transition('\u0030', '\u0033', state12));
		state14.alter(true, 28, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state16));
		state15.alter(true, 9, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\u0030', '\u0030', state17), new Transition('\r', '\r', state37),
				new Transition('\u0031', '\u0039', state16));
		state16.alter(true, 31, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state18));
		state17.alter(true, 18, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0036', state16),
				new Transition('\u0037', '\u0037', state19), new Transition('\u0038', '\u0039', state18));
		state18.alter(true, 38, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state20));
		state19.alter(true, 6, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0034', '\u0039', state20),
				new Transition('\u0030', '\u0032', state18), new Transition('\u0033', '\u0033', state21));
		state20.alter(true, 7, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state22));
		state21.alter(true, 24, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0036', state20),
				new Transition('\u0037', '\u0037', state23), new Transition('\u0038', '\u0039', state22));
		state22.alter(true, 13, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state24));
		state23.alter(true, 1, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\u0030', '\u0030', state25), new Transition('\r', '\r', state37),
				new Transition('\u0031', '\u0039', state24));
		state24.alter(true, 25, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state26));
		state25.alter(true, 10, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0038', state24),
				new Transition('\u0039', '\u0039', state27));
		state26.alter(true, 21, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state28));
		state27.alter(true, 34, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0036', '\u0039', state28),
				new Transition('\u0035', '\u0035', state29), new Transition('\u0030', '\u0034', state26));
		state28.alter(true, 35, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state30));
		state29.alter(true, 2, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0036', '\u0039', state30),
				new Transition('\u0035', '\u0035', state31), new Transition('\u0030', '\u0034', state28));
		state30.alter(true, 39, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state32));
		state31.alter(true, 19, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\u0030', '\u0030', state30), new Transition('\r', '\r', state37),
				new Transition('\u0031', '\u0031', state33), new Transition('\u0032', '\u0039', state32));
		state32.alter(true, 3, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state34));
		state33.alter(true, 14, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0037', '\u0039', state34),
				new Transition('\u0036', '\u0036', state35), new Transition('\u0030', '\u0035', state32));
		state34.alter(true, 22, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0039', state37));
		state35.alter(true, 36, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\u0030', '\u0030', state34), new Transition('\r', '\r', state37),
				new Transition('\u0031', '\u0031', state36), new Transition('\u0032', '\u0039', state37));
		state36.alter(true, 30, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37), new Transition('\u0030', '\u0035', state37));
		state37.alter(true, 5, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\r', '\r', state37));
		state38.alter(false, 17, new Transition('\u0009', '\n', state38), new Transition('\u0020', '\u0020', state38),
				new Transition('\u0030', '\u0030', state39), new Transition('\r', '\r', state38),
				new Transition('\u0031', '\u0031', state1), new Transition('\u0032', '\u0039', state0));
		state39.alter(true, 37, new Transition('\u0009', '\n', state37), new Transition('\u0020', '\u0020', state37),
				new Transition('\u0030', '\u0030', state39), new Transition('\r', '\r', state37),
				new Transition('\u0031', '\u0031', state1), new Transition('\u0032', '\u0039', state0));
		return Automaton.load(state38, true, null);
	}

	private static Automaton getAut180() {
		State state0 = new State();
		State state1 = new State();
		State state2 = new State();
		State state3 = new State();
		State state4 = new State();
		State state5 = new State();
		State state6 = new State();
		State state7 = new State();
		State state8 = new State();
		State state9 = new State();
		State state10 = new State();
		state0.alter(true, 7, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\u0030', '\u0030', state0), new Transition('\r', '\r', state8),
				new Transition('\u0031', '\u0035', state10), new Transition('\u0037', '\u0039', state2),
				new Transition('\u0036', '\u0036', state1));
		state1.alter(true, 1, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0036', '\u0039', state4),
				new Transition('\u0035', '\u0035', state3), new Transition('\u0030', '\u0034', state2));
		state2.alter(true, 10, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0030', '\u0039', state4));
		state3.alter(true, 9, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0036', '\u0039', state6),
				new Transition('\u0035', '\u0035', state5), new Transition('\u0030', '\u0034', state4));
		state4.alter(true, 2, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0030', '\u0039', state6));
		state5.alter(true, 3, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0034', '\u0039', state8),
				new Transition('\u0030', '\u0032', state6), new Transition('\u0033', '\u0033', state7));
		state6.alter(true, 5, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0030', '\u0039', state8));
		state7.alter(true, 6, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0030', '\u0035', state8));
		state8.alter(true, 4, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8));
		state9.alter(false, 0, new Transition('\u0009', '\n', state9), new Transition('\u0020', '\u0020', state9),
				new Transition('\u0030', '\u0030', state0), new Transition('\r', '\r', state9),
				new Transition('\u0031', '\u0035', state10), new Transition('\u0037', '\u0039', state2),
				new Transition('\u0036', '\u0036', state1));
		state10.alter(true, 8, new Transition('\u0009', '\n', state8), new Transition('\u0020', '\u0020', state8),
				new Transition('\r', '\r', state8), new Transition('\u0030', '\u0039', state2));
		return Automaton.load(state9, true, null);
	}

	private static Automaton getAut181() {
		State state0 = new State();
		state0.alter(true, 0, new Transition('\u0009', '\n', state0), new Transition('\u0020', '\u0020', state0),
				new Transition('\r', '\r', state0));
		return Automaton.load(state0, true, null);
	}

	private static Automaton getAut182() {
		State state0 = new State();
		State state1 = new State();
		state0.alter(false, 1, new Transition('\u0009', '\n', state1), new Transition('\u0020', '\u0020', state1),
				new Transition('\r', '\r', state1));
		state1.alter(true, 0);
		return Automaton.load(state0, true, null);
	}

	private static Automaton load(String name) {
		try {
			URL url = Datatypes.class.getClassLoader().getResource("aut/" + name + ".aut");
			return Automaton.load(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void store(String name, Automaton a) {
		String dir = System.getProperty("dk.brics.automaton.datatypes");
		if (dir == null)
			dir = "src/aut";
		try {
			a.store((new FileOutputStream(dir + "/" + name + ".aut")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void buildAll() {
		put(automata, "AlphabeticPresentationForms", getAut1());
		put(automata, "Arabic", getAut2());
		put(automata, "ArabicPresentationForms-A", getAut3());
		put(automata, "ArabicPresentationForms-B", getAut4());
		put(automata, "Armenian", getAut5());
		put(automata, "Arrows", getAut6());
		put(automata, "BasicLatin", getAut7());
		put(automata, "Bengali", getAut8());
		put(automata, "BlockElements", getAut9());
		put(automata, "Bopomofo", getAut10());
		put(automata, "BopomofoExtended", getAut11());
		put(automata, "BoxDrawing", getAut12());
		put(automata, "BraillePatterns", getAut13());
		put(automata, "ByzantineMusicalSymbols", getAut14());
		put(automata, "C", getAut15());
		put(automata, "Other", getAut15());
		put(automata, "CJKCompatibility", getAut16());
		put(automata, "CJKCompatibilityForms", getAut17());
		put(automata, "CJKCompatibilityIdeographs", getAut18());
		put(automata, "CJKCompatibilityIdeographsSupplement", getAut19());
		put(automata, "CJKRadicalsSupplement", getAut20());
		put(automata, "CJKSymbolsandPunctuation", getAut21());
		put(automata, "CJKUnifiedIdeographs", getAut22());
		put(automata, "CJKUnifiedIdeographsExtensionA", getAut23());
		put(automata, "CJKUnifiedIdeographsExtensionB", getAut24());
		put(automata, "Cc", getAut25());
		put(automata, "Cf", getAut26());
		put(automata, "Char", getAut27());
		put(automata, "Cherokee", getAut28());
		put(automata, "Cn", getAut29());
		put(automata, "Co", getAut30());
		put(automata, "CombiningDiacriticalMarks", getAut31());
		put(automata, "CombiningHalfMarks", getAut32());
		put(automata, "CombiningMarksforSymbols", getAut33());
		put(automata, "ControlPictures", getAut34());
		put(automata, "Cs", getAut35());
		put(automata, "CurrencySymbols", getAut36());
		put(automata, "Cyrillic", getAut37());
		put(automata, "Deseret", getAut38());
		put(automata, "Devanagari", getAut39());
		put(automata, "Dingbats", getAut40());
		put(automata, "EnclosedAlphanumerics", getAut41());
		put(automata, "EnclosedCJKLettersandMonths", getAut42());
		put(automata, "Ethiopic", getAut43());
		put(automata, "GeneralPunctuation", getAut44());
		put(automata, "GeometricShapes", getAut45());
		put(automata, "Georgian", getAut46());
		put(automata, "Gothic", getAut47());
		put(automata, "Greek", getAut48());
		put(automata, "GreekExtended", getAut49());
		put(automata, "Gujarati", getAut50());
		put(automata, "Gurmukhi", getAut51());
		put(automata, "HalfwidthandFullwidthForms", getAut52());
		put(automata, "HangulCompatibilityJamo", getAut53());
		put(automata, "HangulJamo", getAut54());
		put(automata, "HangulSyllables", getAut55());
		put(automata, "Hebrew", getAut56());
		put(automata, "Hiragana", getAut57());
		put(automata, "IPAExtensions", getAut58());
		put(automata, "IdeographicDescriptionCharacters", getAut59());
		put(automata, "Kanbun", getAut60());
		put(automata, "KangxiRadicals", getAut61());
		put(automata, "Kannada", getAut62());
		put(automata, "Katakana", getAut63());
		put(automata, "Khmer", getAut64());
		put(automata, "L", getAut65());
		put(automata, "Letter", getAut65());
		put(automata, "Lao", getAut66());
		put(automata, "Latin-1Supplement", getAut67());
		put(automata, "LatinExtended-A", getAut68());
		put(automata, "LatinExtended-B", getAut69());
		put(automata, "LatinExtendedAdditional", getAut70());
		put(automata, "Letter", getAut71());
		put(automata, "LetterlikeSymbols", getAut72());
		put(automata, "Ll", getAut73());
		put(automata, "Lm", getAut74());
		put(automata, "Lo", getAut75());
		put(automata, "Lt", getAut76());
		put(automata, "Lu", getAut77());
		put(automata, "M", getAut78());
		put(automata, "Mark", getAut78());
		put(automata, "Malayalam", getAut79());
		put(automata, "MathematicalAlphanumericSymbols", getAut80());
		put(automata, "MathematicalOperators", getAut81());
		put(automata, "Mc", getAut82());
		put(automata, "Me", getAut83());
		put(automata, "MiscellaneousSymbols", getAut84());
		put(automata, "MiscellaneousTechnical", getAut85());
		put(automata, "Mn", getAut86());
		put(automata, "Mongolian", getAut87());
		put(automata, "MusicalSymbols", getAut88());
		put(automata, "Myanmar", getAut89());
		put(automata, "N", getAut90());
		put(automata, "Number", getAut90());
		put(automata, "NCName", getAut91());
		put(automata, "NCName2", getAut92());
		put(automata, "NCNames", getAut93());
		put(automata, "Name2", getAut94());
		put(automata, "NameChar", getAut95());
		put(automata, "Names", getAut96());
		put(automata, "Nd", getAut97());
		put(automata, "Nl", getAut98());
		put(automata, "Nmtoken2", getAut99());
		put(automata, "Nmtokens", getAut100());
		put(automata, "No", getAut101());
		put(automata, "NumberForms", getAut102());
		put(automata, "Ogham", getAut103());
		put(automata, "OldItalic", getAut104());
		put(automata, "OpticalCharacterRecognition", getAut105());
		put(automata, "Oriya", getAut106());
		put(automata, "P", getAut107());
		put(automata, "Punctuation", getAut107());
		put(automata, "Pc", getAut108());
		put(automata, "Pd", getAut109());
		put(automata, "Pe", getAut110());
		put(automata, "Pf", getAut111());
		put(automata, "Pi", getAut112());
		put(automata, "Po", getAut113());
		put(automata, "PrivateUse", getAut114());
		put(automata, "Ps", getAut115());
		put(automata, "QName", getAut116());
		put(automata, "QName2", getAut117());
		put(automata, "Runic", getAut118());
		put(automata, "S", getAut119());
		put(automata, "Symbol", getAut119());
		put(automata, "Sc", getAut120());
		put(automata, "Sinhala", getAut121());
		put(automata, "Sk", getAut122());
		put(automata, "Sm", getAut123());
		put(automata, "SmallFormVariants", getAut124());
		put(automata, "So", getAut125());
		put(automata, "SpacingModifierLetters", getAut126());
		put(automata, "Specials", getAut127());
		put(automata, "SuperscriptsandSubscripts", getAut128());
		put(automata, "Syriac", getAut129());
		put(automata, "Tags", getAut130());
		put(automata, "Tamil", getAut131());
		put(automata, "Telugu", getAut132());
		put(automata, "Thaana", getAut133());
		put(automata, "Thai", getAut134());
		put(automata, "Tibetan", getAut135());
		put(automata, "URI", getAut136());
		put(automata, "UnifiedCanadianAboriginalSyllabics", getAut137());
		put(automata, "YiRadicals", getAut138());
		put(automata, "YiSyllables", getAut139());
		put(automata, "Z", getAut140());
		put(automata, "Separator", getAut140());
		put(automata, "Zl", getAut141());
		put(automata, "Zp", getAut142());
		put(automata, "Zs", getAut143());
		put(automata, "_digit", getAut144());
		put(automata, "_horizontal", getAut145());
		put(automata, "_linebreak", getAut146());
		put(automata, "_space", getAut147());
		put(automata, "_vertical", getAut148());
		put(automata, "_word", getAut149());
		put(automata, "anyname", getAut150());
		put(automata, "base64Binary", getAut151());
		put(automata, "boolean", getAut152());
		put(automata, "byte", getAut153());
		put(automata, "date", getAut154());
		put(automata, "dateTime", getAut155());
		put(automata, "decimal", getAut156());
		put(automata, "duration", getAut157());
		put(automata, "float", getAut158());
		put(automata, "gDay", getAut159());
		put(automata, "gMonth", getAut160());
		put(automata, "gMonthDay", getAut161());
		put(automata, "gYear", getAut162());
		put(automata, "gYearMonth", getAut163());
		put(automata, "hexBinary", getAut164());
		put(automata, "int", getAut165());
		put(automata, "integer", getAut166());
		put(automata, "language", getAut167());
		put(automata, "long", getAut168());
		put(automata, "negativeInteger", getAut169());
		put(automata, "noap", getAut170());
		put(automata, "nonNegativeInteger", getAut171());
		put(automata, "nonPositiveInteger", getAut172());
		put(automata, "positiveInteger", getAut173());
		put(automata, "short", getAut174());
		put(automata, "string", getAut175());
		put(automata, "time", getAut176());
		put(automata, "unsignedByte", getAut177());
		put(automata, "unsignedInt", getAut178());
		put(automata, "unsignedLong", getAut179());
		put(automata, "unsignedShort", getAut180());
		put(automata, "whitespace", getAut181());
		put(automata, "whitespacechar", getAut182());

		put(automata, "_digit", getAut144());
		put(automata, "_horizontal", getAut145());
		put(automata, "_linebreak", getAut146());
		put(automata, "_space", getAut147());
		put(automata, "_vertical", getAut148());
		put(automata, "_word", getAut149());
	}

	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		boolean b = Automaton.setAllowMutate(true);
		buildAll();
		Automaton.setAllowMutate(b);
		System.out.println("Storing automata...");
		for (Map.Entry<String, Automaton> e : automata.entrySet())
			store(e.getKey(), e.getValue());
		System.out.println("Time for building automata: " + (System.currentTimeMillis() - t) + "ms");
	}

}
