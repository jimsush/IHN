# -*- coding: UTF-8 -*-

import requests
import re
import json


			
def parse_comments(category):
	f= open('j-comment'+str(category)+'.csv', 'w')
	f1 = open('j-id'+str(category)+'.txt', 'r')
	list1 = f1.readlines()
	f1.close()

	sku_ids=''
	for i in range(0, len(list1)):
		list1[i] = list1[i].rstrip('\n')
		print('sku_id: '+list1[i]+" i="+str(i))
		if i%20==0:
			sku_ids=list1[i]
		else:
			sku_ids=sku_ids+','+list1[i]
		if i%20==19 or i==(len(list1)-1) :
			print('sku_ids: '+sku_ids)
			resp=requests.get('https://club..com/comment/.action?my=pinglun&referenceIds='+sku_ids+'&callback=jQuery7706615&_=1536334749531')
			pos1=resp.text.find('(')
			pos2=resp.text.find(')')
			json_str=resp.text[pos1+1: pos2]
			json_obj=json.loads(json_str)['CommentsCount']
			print('json len='+str(len(json_obj)))
			for j in range(0, len(json_obj)):
				sku_id=json_obj[j]['SkuId']
				comment_count=json_obj[j]['CommentCount']
				f.write('"'+str(sku_id)+'","'+str(comment_count)+'"\n')
	f.close()
	print('parse_comments done')
			

def parse_price(category): 
	f= open('j-price'+str(category)+'.csv', 'w')
	f1 = open('j-id'+str(category)+'.txt', 'r')
	list1 = f1.readlines()
	f1.close()
	
	sku_ids=''
	for i in range(0, len(list1)):
		list1[i] = list1[i].rstrip('\n')
		print('sku_id: '+list1[i]+" i="+str(i))
		if i%20==0:
			sku_ids='J_'+list1[i]
		else:
			sku_ids=sku_ids+'%2CJ_'+list1[i]
		if i%20==19 or i==(len(list1)-1) :
			print('sku_ids: '+sku_ids)
			resp=requests.get('https://.cn/prices/mg?callback=jQuery2616268&ext=11000000&pin=&type=1&area=1_72_4137_0&skuIds='+sku_ids+'&pdbp=0&pdtk=&pdpin=&pduid=1533302806972322044401&source=list_pc_front&_=1536389818794')
			pos1=resp.text.find('[')
			pos2=resp.text.find(']')
			json_str=resp.text[pos1 : pos2+1]
			json_obj=json.loads(json_str)
			print('json len='+str(len(json_obj)))
			for j in range(0, len(json_obj)):
				sku_id=json_obj[j]['id']
				sku_id=sku_id[2: len(sku_id)]
				price=json_obj[j]['p']
				f.write('"'+sku_id+'","'+price+'"\n')
	f.close()
	print('parse_price done')			
			
# 1:冷冻 乳2
def parse_ids(category):
	f = open('j-id'+str(category)+'.txt', 'w')
	total_page=[45,19] 
	url_first=['https://list..com/lit.html?cat=12218,13591&page=','https://list..com/lis.html?cat=12218,13598&page=']
	url_second=['&delivery=1&stock=0&sort=sort_rank_asc&trans=1&JL=4_7_0#J_main','&delivery=1&stock=0&sort=sort_rank_asc&trans=1&JL=4_7_0#J_main']
	for pageno in range(1,total_page[category-1]):
		resp=requests.get(url_first[category-1]+str(pageno)+url_second[category-1])
		print('page '+str(pageno+1))
		pos1=resp.text.find("var attrList =")
		pos2=resp.text.find("var other_exts =")
		line=resp.text[pos1+14:pos2]
		sku_ids=re.findall('\d+:{',line)
		for sku_id in sku_ids:
			sku_id=sku_id[0:len(sku_id)-2]
			if len(sku_id)==7:
				f.write(sku_id+'\n')
	f.close()
	print('parse ids done')


def parse_props(category):
	f = open('j-detail'+str(category)+'.csv', 'w')
	f1 = open('j-id'+str(category)+'.txt', 'r')
	list1 = f1.readlines()
	f1.close()

	for i in range(0, len(list1)):
		list1[i] = list1[i].rstrip('\n')
		sku_id=list1[i]
		print('start '+str(i+1)+'. '+sku_id)
		resp=requests.get('https://ite..com/'+sku_id+'.html')
		
		pos_brand1=resp.text.find('p-parameter-list')
		pos_brand2=resp.text.find('\'>品牌', pos_brand1)
		line_brand=resp.text[pos_brand1+18:pos_brand2].replace('\n','')
		pos_brand3=line_brand.find('\'')
		brand=line_brand[pos_brand3+1:len(line_brand)]
		
		pos1=resp.text.find('parameter2 p-parameter-list')
		pos2=resp.text.find('ul',pos1)
		
		line=resp.text[pos1+30:pos2-6].replace('\n','')
		match_strs=re.findall('商品名称：.*</li>',line)
		if len(match_strs)>0:
			name_pos=match_strs[0].find('</li>')
			goods_name=match_strs[0][5:name_pos]
		else:
			goods_name=''
			
		match_strs=re.findall('商品毛重：.*</li>',line)
		if len(match_strs)>0:
			weight_pos=match_strs[0].find('</li>')
			weight=match_strs[0][5:weight_pos]
		else:
			weight=''
			
		
		match_strs=re.findall('商品产地：.*</li>',line)
		if len(match_strs)>0:
			area_pos=match_strs[0].find('</li>')
			area=match_strs[0][5:area_pos]
		else:
			area=''
			
		
		match_strs=re.findall('分类：.*</li>',line)
		if len(match_strs)>0:
			category_pos=match_strs[0].find('</li>')
			category=match_strs[0][3:category_pos]
		else:
			category=''
			
		match_strs=re.findall('风味：.*</li>',line)
		if len(match_strs)>0:
			taste_pos=match_strs[0].find('</li>')
			taste=match_strs[0][3:taste_pos]
		else:
			taste=''
		
		f.write('"'+sku_id+'","'+goods_name+'","'+brand+'","'+weight+'","'+area+'","'+category+'","'+taste+'"\n')
	f.close()
	print('parse_props done')
	
	
if __name__ == "__main__":
	#parse_ids(1)
	#parse_props(1)
	#parse_price(1)
	#parse_comments(1)
	
	#parse_ids(2)
	#parse_props(2)
	#parse_price(2)
	#parse_comments(2)
	print('capture data')		

	
	
