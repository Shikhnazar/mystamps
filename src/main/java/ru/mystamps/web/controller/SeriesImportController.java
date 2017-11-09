/*
 * Copyright (C) 2009-2017 Slava Semushin <slava.semushin@gmail.com>
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
package ru.mystamps.web.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

import ru.mystamps.web.Url;
import ru.mystamps.web.controller.converter.annotation.CurrentUser;
import ru.mystamps.web.controller.dto.RequestImportForm;
import ru.mystamps.web.controller.event.ImportRequestCreated;
import ru.mystamps.web.dao.dto.ImportRequestDto;
import ru.mystamps.web.dao.dto.ParsedDataDto;
import ru.mystamps.web.service.SeriesImportService;
import ru.mystamps.web.util.LocaleUtils;

import static ru.mystamps.web.controller.ControllerUtils.redirectTo;

@Controller
@RequiredArgsConstructor
public class SeriesImportController {
	
	private final SeriesImportService seriesImportService;
	private final ApplicationEventPublisher eventPublisher;
	
	@GetMapping(Url.REQUEST_IMPORT_SERIES_PAGE)
	public void showForm(Model model) {
		RequestImportForm requestImportForm = new RequestImportForm();
		model.addAttribute("requestImportForm", requestImportForm);
	}
	
	@PostMapping(Url.REQUEST_IMPORT_SERIES_PAGE)
	public String processInput(
		@Valid RequestImportForm form,
		BindingResult result,
		@CurrentUser Integer currentUserId) {
		
		if (result.hasErrors()) {
			return null;
		}
		
		Integer requestId = seriesImportService.add(form, currentUserId);
		
		ImportRequestCreated requestCreated =
			new ImportRequestCreated(this, requestId, form.getUrl());
		eventPublisher.publishEvent(requestCreated);
		
		return redirectTo(Url.REQUEST_IMPORT_PAGE, requestId);
	}
	
	@GetMapping(Url.REQUEST_IMPORT_PAGE)
	public String showRequest(
		@PathVariable("id") Integer requestId,
		Model model,
		Locale userLocale,
		HttpServletResponse response)
		throws IOException {
		
		if (requestId == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		ImportRequestDto request = seriesImportService.findById(requestId);
		if (request == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		model.addAttribute("request", request);

		String lang = LocaleUtils.getLanguageOrNull(userLocale);
		ParsedDataDto parsedData = seriesImportService.getParsedData(requestId, lang);
		model.addAttribute("parsedData", parsedData);
		
		return "series/import/info";
	}
	
}
