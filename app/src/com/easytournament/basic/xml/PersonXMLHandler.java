package com.easytournament.basic.xml;

import java.awt.Image;
import java.util.Date;

import org.jdom.Element;

import com.easytournament.basic.valueholder.Person;

public class PersonXMLHandler {

  public static void writePerson(Person p, Element playerEl) {
    playerEl.setAttribute("gender", p.getGender() + "");
    playerEl.setAttribute("prename", p.getPrename() == null ? "" : p.getPrename());
    playerEl.setAttribute("name", p.getName() == null ? "" : p.getName());
    playerEl.setAttribute("address", p.getAddress() == null ? "" : p.getAddress());
    playerEl.setAttribute("bdate", p.getBdate().getTime() + "");
    playerEl.setAttribute("email", p.getEmail() == null ? "" : p.getEmail());
    playerEl.setAttribute("phone", p.getPhone() == null ? "" : p.getPhone());
    playerEl.setAttribute("notes", p.getNotes() == null ? "" : p.getNotes());
    if (p.getPicture() != null) {
      Element picEl = new Element("picture");
      Image logo = p.getPicture().getImage();
      ImageEncoder.encodeImage(picEl, logo);
      playerEl.addContent(picEl);
    }
  }

  public static void readPerson(Element pEl, Person p) {
    p.setGender(Short.parseShort(pEl.getAttributeValue("gender")));
    p.setName(pEl.getAttributeValue("name"));
    p.setPrename(pEl.getAttributeValue("prename"));
    p.setAddress(pEl.getAttributeValue("address"));
    p.setBdate(new Date(Long.valueOf(pEl.getAttributeValue("bdate"))));
    p.setEmail(pEl.getAttributeValue("email"));
    p.setPhone(pEl.getAttributeValue("phone"));
    p.setNotes(pEl.getAttributeValue("notes"));

    Element picEl = pEl.getChild("picture");
    if (picEl != null) {
      p.setPicture(ImageEncoder.decodeImage(picEl));
    }
  }
}
