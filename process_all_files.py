#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
import re
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor
import sys

def remove_comments_java(content):
    """删除Java注释"""
    result = []
    lines = content.split('\n')
    in_block_comment = False
    
    for line in lines:
        if in_block_comment:
            if '*/' in line:
                idx = line.find('*/')
                line = line[idx+2:].strip()
                in_block_comment = False
            else:
                continue
        
        while '/*' in line:
            start = line.find('/*')
            if '*/' in line[start:]:
                end = line.find('*/', start)
                line = line[:start] + line[end+2:]
            else:
                line = line[:start]
                in_block_comment = True
                break
        
        if '//' in line and not in_block_comment:
            idx = line.find('//')
            line = line[:idx].rstrip()
        
        if line.strip() or not result:
            result.append(line)
    
    return '\n'.join(result)

def remove_comments_js(content):
    """删除JS/TS注释"""
    return remove_comments_java(content)

def remove_comments_vue(content):
    """删除Vue注释"""
    content = re.sub(r'<!--.*?-->', '', content, flags=re.DOTALL)
    return remove_comments_js(content)

def remove_comments_sql(content):
    """删除SQL注释"""
    result = []
    lines = content.split('\n')
    in_block_comment = False
    
    for line in lines:
        if in_block_comment:
            if '*/' in line:
                idx = line.find('*/')
                line = line[idx+2:].strip()
                in_block_comment = False
            else:
                continue
        
        if '/*' in line:
            start = line.find('/*')
            if '*/' in line[start:]:
                end = line.find('*/', start)
                line = line[:start] + line[end+2:]
            else:
                line = line[:start]
                in_block_comment = True
        
        if '--' in line and not in_block_comment:
            idx = line.find('--')
            line = line[:idx].rstrip()
        
        result.append(line)
    
    return '\n'.join(result)

def remove_comments_css(content):
    """删除CSS注释"""
    return remove_comments_js(content)

def update_author(content):
    """更新作者信息"""
    content = re.sub(r'@author\s+[^\n]*', '@author maoshun tian', content, flags=re.IGNORECASE)
    content = re.sub(r'"author"\s*:\s*"[^"]*"', '"author": "maoshun tian"', content, flags=re.IGNORECASE)
    content = re.sub(r"'author'\s*:\s*'[^']*'", "'author': 'maoshun tian'", content, flags=re.IGNORECASE)
    return content

def process_file(file_path):
    """处理单个文件"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original = content
        ext = file_path.suffix.lower()
        
        if ext == '.java':
            content = remove_comments_java(content)
        elif ext in ['.js', '.ts', '.jsx', '.tsx']:
            content = remove_comments_js(content)
        elif ext == '.vue':
            content = remove_comments_vue(content)
        elif ext == '.sql':
            content = remove_comments_sql(content)
        elif ext in ['.scss', '.css']:
            content = remove_comments_css(content)
        elif ext == '.vm':
            content = remove_comments_java(content)
        
        content = update_author(content)
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        content = re.sub(r'^\n+', '', content)
        content = re.sub(r'\n+$', '\n', content)
        
        if content != original:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        return f"ERROR: {e}"

def main():
    base_dir = Path(__file__).parent
    extensions = ['.java', '.js', '.ts', '.vue', '.sql', '.scss', '.css', '.vm', '.json']
    exclude_dirs = {'node_modules', '.git', 'target', 'dist', 'build', '.idea', '.vscode', '__pycache__', '.mvn'}
    
    files = []
    for root, dirs, filenames in os.walk(base_dir):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        for filename in filenames:
            file_path = Path(root) / filename
            if file_path.suffix.lower() in extensions:
                files.append(file_path)
    
    total = len(files)
    processed = 0
    errors = 0
    
    print(f"找到 {total} 个文件需要处理\n")
    print("=" * 60)
    
    for i, file_path in enumerate(files, 1):
        result = process_file(file_path)
        if result is True:
            processed += 1
            print(f"[{i}/{total}] ✓ {file_path.name}")
        elif result is False:
            print(f"[{i}/{total}] - {file_path.name} (无变化)")
        else:
            errors += 1
            print(f"[{i}/{total}] ✗ {file_path.name} ({result})")
        
        if i % 50 == 0:
            print(f"\n进度: {i}/{total} ({i*100//total}%) | 已处理: {processed} | 错误: {errors}\n")
    
    print("=" * 60)
    print(f"\n完成！总计: {total} | 已处理: {processed} | 无变化: {total-processed-errors} | 错误: {errors}")

if __name__ == '__main__':
    main()

