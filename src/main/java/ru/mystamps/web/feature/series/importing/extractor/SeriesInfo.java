/*
 * Copyright (C) 2009-2019 Slava Semushin <slava.semushin@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package ru.mystamps.web.feature.series.importing.extractor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.mystamps.web.feature.series.importing.sale.SeriesSaleInfo;

/**
 * Representation of a series info.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SeriesInfo {
	private String categoryName;
	private String countryName;
	private String imageUrl;
	private String issueDate;
	private String quantity;
	private String perforated;
	private String michelNumbers;
	// the code assumes that saleInfo is always non-null
	private SeriesSaleInfo saleInfo;
	
	/**
	 * Check whether any info about a series is available.
	 */
	public boolean isEmpty() {
		return categoryName == null
			&& countryName == null
			&& imageUrl == null
			&& issueDate == null
			&& quantity == null
			&& perforated == null
			&& michelNumbers == null
			&& saleInfo.isEmpty();
	}
	
	public String getSellerName() {
		return saleInfo.getSellerName();
	}
	
	public String getSellerUrl() {
		return saleInfo.getSellerUrl();
	}
	
	public String getPrice() {
		return saleInfo.getPrice();
	}
	
	public String getCurrency() {
		return saleInfo.getCurrency();
	}
	
}
